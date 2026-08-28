package com.kiero.image.repository

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.kid.schedule.local.datasource.ImageLocalDataSource
import com.kiero.data.kid.schedule.remote.datasource.ImageUploadDataSource
import com.kiero.data.kid.schedule.remote.dto.response.ScheduleImageUploadResponseDto
import com.kiero.data.kid.schedule.repositoryimpl.ImageUploadRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.File

class ImageUploadRepositoryImplTest {
    private lateinit var remoteDataSource: ImageUploadDataSource
    private lateinit var localDataSource: ImageLocalDataSource
    private lateinit var repository: ImageUploadRepositoryImpl

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        localDataSource = mockk(relaxed = true)
        repository = ImageUploadRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `성공 시나리오_이미지_업로드_및_원본_임시파일_삭제가_정상_수행된다`() = runTest {
        val dummyUri = "content://dummy/uri"

        val tempFile = File.createTempFile("test", ".webp").apply {
            deleteOnExit()
        }

        val mockPresignedData = ScheduleImageUploadResponseDto(
            fileName = "test.webp",
            presignedUrl = "https://s3.aws.com/bucket/test.webp?Signature=123"
        )

        val successResponse = BaseResponse(
            status = 200,
            message = "Success",
            data = mockPresignedData
        )

        coEvery { localDataSource.getOptimizedFile(dummyUri) } returns tempFile

        coEvery { remoteDataSource.postPresignedUrl(any(), any()) } returns successResponse
        coEvery { remoteDataSource.uploadImageToS3(any(), any()) } returns Response.success(Unit)

        // When
        val result = repository.uploadImage(dummyUri, "test.webp", "image/webp")

        result.onFailure { exception ->
            println("테스트 실패 원인: ${exception.message}")
            exception.printStackTrace()
        }

        // Then
        assertTrue(result.isSuccess)
        assertEquals("https://s3.aws.com/bucket/test.webp", result.getOrNull())

        coVerify(exactly = 1) { localDataSource.deleteOriginalUri(dummyUri) }
    }

    @Test
    fun `실패 시나리오_S3_업로드중_네트워크_에러가_나더라도_finally에서_임시파일은_반드시_삭제된다`() = runTest {
        val dummyUri = "content://dummy/uri"
        val mockFile = mockk<File>(relaxed = true) {
            every { exists() } returns true
        }
        val mockPresignedData = ScheduleImageUploadResponseDto(
            fileName = "test.webp",
            presignedUrl = "https://s3.aws.com/bucket/test.webp?Signature=123"
        )

        coEvery { localDataSource.getOptimizedFile(dummyUri) } returns mockFile
        coEvery { remoteDataSource.postPresignedUrl(any(), any()) } returns BaseResponse(status = 200, message = "", data = mockPresignedData)
        
        // S3 업로드 시 통신 실패(500 에러)를 가정
        val errorResponse = Response.error<Unit>(500, "Server Error".toResponseBody())
        coEvery { remoteDataSource.uploadImageToS3(any(), any()) } returns errorResponse

        // When
        val result = repository.uploadImage(dummyUri, "test.webp", "image/webp")

        // Then
        assertTrue(result.isFailure) // 결과가 실패로 떨어졌는가

        verify(exactly = 0) { localDataSource.deleteOriginalUri(dummyUri) } // 실패했으니 원본 이미지는 지우면 안댐 (호출 횟수 0번)
        verify(exactly = 1) { mockFile.delete() } // 실패했음에도 불구하고 finally를 타고 임시 캐시 파일은 삭제되었는가?
    }
}
