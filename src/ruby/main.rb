require 'json'
require_relative 'auth'
require_relative 'request'

EXECUTE_BOT_COMMAND = "execute bot"
NEW_TWEET_COMMAND = "new tweet"

# update twitter status
def updateTwitterStatus (status)
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
    HTTPClient.sendRequest(method, baseURL + "?status=#{status}" , { Authorization: authHeader } )
end

# sets environment variables from a file
def setEnv (filePath)
    if filePath.length == 0
        return
    end 
    file = File.open(filePath)
    fileData = file.readlines.map(&:chomp)
    file.close
    fileData.each do |data|
        var = data.split("=")
        ENV[var[0]] = var[1]
    end
end

# prompts user & returns response
def getInput(prompt)
    puts prompt
    gets.chomp
end

# accepts new user tweet
def newTweet 
    status = getInput("What would you like to tweet?")
    updateTwitterStatus (status.strip)
end

# invokes twitter bot to get from API
def invokeBot()
    today = Time.now.strftime("%Y-%m-%d")

    requestParams = {
        "X-RapidAPI-Key": ENV["X_RapidAPI_Key"],
        "X-RapidAPI-Host": ENV["X_RapidAPI_Host"]
    }

    uri = "https://api-nba-v1.p.rapidapi.com/games?date=#{today}"
    puts HTTPClient.sendRequest("GET", uri , requestParams )
end


setEnv("../../.env")
operation = getInput("What do you want to do? (#{EXECUTE_BOT_COMMAND}; #{NEW_TWEET_COMMAND})")

operation.eql? EXECUTE_BOT_COMMAND ? invokeBot() : newTweet()