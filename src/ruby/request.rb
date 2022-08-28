require 'uri'
require 'net/http'
require 'json'

module HTTPClient
    
    # sends HTTP request
    def sendRequest (method, uri, requestOptions = {})

    url = URI (uri)
    http = Net::HTTP.new(url.host, url.port)
    http.use_ssl = true
    http.verify_mode = OpenSSL::SSL::VERIFY_NONE

    request = nil
    case method.upcase
    when "GET"
        request = Net::HTTP::Get.new(url)
    when "POST"
        request = Net::HTTP::Post.new(url)
    else
        raise StandardError.new "Unsupported HTTP Method"
    end

    requestOptions.keys.each do |key|
        request[key] = requestOptions[key]
    end

    response = http.request(request)
    end

end
