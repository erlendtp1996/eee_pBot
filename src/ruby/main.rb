require 'uri'
require 'base64'
require 'net/http'
require 'openssl'
require 'json'

module OAuthAuthentication
    extend self
    def createOAuthHeader (method, baseURL, options = {})
        consumerSecret = options[:consumer_secret]
        accessTokenSecret = options[:access_token_secret]
        options.delete( :consumer_secret )
        options.delete( :access_token_secret )

        parameterString = createParameterString( options )
        baseString = createBaseString( method, baseURL, parameterString)
        signingKey = createSigningKey( consumerSecret, accessTokenSecret)
        signature = createSignature( signingKey, baseString )

        authString = createAuthString( options, signature )
    end

    private

    def encodeString ( str )
        if !str.instance_of? String
            str = str.to_s
        end
        str = URI.escape( str )
        return str.gsub("+", "%2B").gsub(",", "%2C").gsub("!", "%21").gsub(":", "%3A").gsub("/", "%2F").gsub("=", "%3D").gsub("&", "%26")
    end

    def createParameterString ( parameters )
        parameterString = ""
        sortedKeys = parameters.keys.sort
        sortedKeys.each_with_index do |key, index|
            parameterString << encodeString( key ) << "=" << encodeString( parameters[key] ) << (index == sortedKeys.length - 1 ? "" : "&")
        end
        return parameterString
    end
    
    def createBaseString (method, baseURL, parameterString)
        return method.upcase + "&" + encodeString( baseURL ) + "&" + encodeString( parameterString )
    end
    
    def createSigningKey ( consumerSecret, accessTokenSecret ) 
        return encodeString( consumerSecret ) + "&" + encodeString( accessTokenSecret )
    end 
    
    def createSignature ( signingKey, baseString )
        return Base64.encode64("#{OpenSSL::HMAC.digest('sha1', signingKey, baseString)}").strip.gsub("+", "%2B").gsub("=", "%3D")
    end
    
    def createAuthString( parameters, signature )
        authString = "OAuth "
        parameters.delete(:status)
        parameters = parameters.merge({ oauth_signature: signature })
        sortedKeys = parameters.keys.sort
        sortedKeys.each_with_index do |key, index|
            authString << key.to_s << "=" << '"' << parameters[key] << '"' << (index == sortedKeys.length - 1 ? "" : ',')
        end
        return authString
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
        oauth_version: "1.0",
        consumer_secret: ENV["CONSUMER_SECRET"],
        access_token_secret: ENV["ACCESS_TOKEN_SECRET"]
    }

    authHeader = OAuthAuthentication.createOAuthHeader( method, baseURL, params )
    baseURL = baseURL + "?status=#{status}"
    sendPostRequest( baseURL , { Authorization: authHeader } )
end

def sendPostRequest (uri, requestOptions = {})
    url = URI (uri)
    http = Net::HTTP.new(url.host, url.port)

    request = Net::HTTP::Post.new(url)
    http.use_ssl = true
    http.verify_mode = OpenSSL::SSL::VERIFY_NONE
    
    requestOptions.keys.each do |key|
        request[key] = requestOptions[key]
    end

    puts request["Authorization"]

    response = http.request(request)
    response.code
    puts response
end

def setEnv (filePath)
    file = File.open(filePath)
    fileData = file.readlines.map(&:chomp)
    file.close
    fileData.each do |data|
        var = data.split("=")
        ENV[var[0]] = var[1]
    end
end

setEnv("../../.env")

puts "What would you like to tweet?"
status = gets 

updateStatus (status.strip)