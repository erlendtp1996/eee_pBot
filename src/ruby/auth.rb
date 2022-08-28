require 'openssl'
require 'json'
require 'base64'

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