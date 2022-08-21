require 'uri'
require 'base64'
require 'net/http'
require 'cgi'
require 'openssl'
require 'json'

def setEnv (filePath)
    file = File.open(filePath)
    fileData = file.readlines.map(&:chomp)
    file.close
    fileData.each do |data|
        var = data.split("=")
        ENV[var[0]] = var[1]
    end
end

def updateStatus (status)
    method = "POST"
    baseURL = "https://api.twitter.com/1.1/statuses/update.json"

    timestamp = Time.now.to_i.to_s

    params = {
        status: status,
        oauth_consumer_key: ENV["CONSUMER_KEY"],
        oauth_nonce: "kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg",
        oauth_signature_method: "HMAC-SHA1",
        oauth_timestamp: timestamp, 
        oauth_token: ENV["ACCESS_TOKEN"],
        oauth_version: "1.0"
    }

    parameterString = createParameterString(params)
    baseString = createBaseString( method, baseURL, parameterString)
    signingKey = createSigningKey( ENV["CONSUMER_SECRET"], ENV["ACCESS_TOKEN_SECRET"])
    signature = createSignature( signingKey, baseString )

    authString = createAuthString( params, signature )
    sendRequest(baseURL, status, authString)
end

def sendRequest (baseURL, status, authString)

    url = URI (baseURL + "?status=#{status}")
    http = Net::HTTP.new(url.host, url.port)

    request = Net::HTTP::Post.new(url)
    http.use_ssl = true
    http.verify_mode = OpenSSL::SSL::VERIFY_NONE
    request['Authorization'] = authString
    response = http.request(request)
    puts response.to_s
end

def encodeString (str)
    if !str.instance_of? String
        str = str.to_s
    end
    str = URI.escape(str)
    return str.gsub("+", "%2B").gsub(",", "%2C").gsub("!", "%21").gsub(":", "%3A").gsub("/", "%2F").gsub("=", "%3D").gsub("&", "%26")
end

def createParameterString (parameters)
    parameterString = ""
    sortedKeys = parameters.keys.sort
    sortedKeys.each_with_index do |key, index|
        parameterString << encodeString(key) << "=" << encodeString(parameters[key]) << (index == sortedKeys.length - 1 ? "" : "&")
    end
    return parameterString
end

def createBaseString (method, baseURL, parameterString)
    return method.upcase + "&" + encodeString(baseURL) + "&" + encodeString(parameterString)
end

def createSigningKey (consumerSecret, oauthSecret) 
    encodeString(consumerSecret) + "&" + encodeString(oauthSecret)
end 

def createSignature (key, signature)
    Base64.encode64("#{OpenSSL::HMAC.digest('sha1', key, signature)}").strip.gsub("+", "%2B").gsub("=", "%3D")
end

def createAuthString(parameter, signature)
    authString = "OAuth "
    parameter.delete(:status)
    parameter = parameter.merge({ oauth_signature: signature })

    sortedKeys = parameter.keys.sort

    sortedKeys.each_with_index do |key, index|
        authString << key.to_s << "=" << '"' << parameter[key] << '"' << (index == sortedKeys.length - 1 ? "" : ',')
    end

    return authString
end

setEnv("../../.env")

puts "What would you like to tweet?"
status = gets 

updateStatus (status.strip)