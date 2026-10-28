# 임시: eMudhra 중간 CA 신뢰 여부 확인 (확인 후 삭제)
require 'net/http'
require 'openssl'
require 'uri'

HOSTS = %w[
  https://dv-g3b-emudhra.we-have-a-problem.com/
  https://dv-g3b-noct-emudhra.we-have-a-problem.com/
].freeze

def verdict(name, url)
  yield
  puts "[TRUSTS]   #{name} #{url}"
rescue OpenSSL::SSL::SSLError, Excon::Error::Socket, HTTPClient::ConnectTimeoutError => e
  puts "[rejected] #{name} #{url} (#{e.class})"
rescue StandardError => e
  puts "[error]    #{name} #{url} (#{e.class}: #{e.message[0, 120]})"
end

%w[excon httpclient].each do |lib|
  begin
    require lib
  rescue LoadError
    puts "[skip] #{lib} not available"
  end
end

HOSTS.each do |url|
  uri = URI(url)
  verdict('net/http', url) do
    Net::HTTP.start(uri.host, uri.port, use_ssl: true, open_timeout: 10, read_timeout: 10) { |h| h.head('/') }
  end
  if defined?(Excon)
    verdict('excon', url) { Excon.head(url, connect_timeout: 10, read_timeout: 10, ssl_verify_peer: true) }
  end
  if defined?(HTTPClient)
    verdict('httpclient', url) { HTTPClient.new.tap { |c| c.connect_timeout = 10 }.head(url) }
  end
end
puts "ruby=#{RUBY_VERSION} openssl=#{OpenSSL::OPENSSL_LIBRARY_VERSION} excon=#{defined?(Excon) ? Excon::VERSION : 'n/a'}"
