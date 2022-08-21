
=begin
domain = "https://api-nba-v1.p.rapidapi.com"
path = "/games"

# load environment variables
today = Time.now.strftime("%Y-%m-%d")

parameters = "?date=#{today}"

# send request to api
url = URI (domain + path + parameters)

http = Net::HTTP.new(url.host, url.port)
http.use_ssl = true
http.verify_mode = OpenSSL::SSL::VERIFY_NONE

request = Net::HTTP::Get.new(url)
request["X-RapidAPI-Key"] = ENV["X_RapidAPI_Key"]
request["X-RapidAPI-Host"] = ENV["X_RapidAPI_Host"]

response = http.request(request)
parsedResponse = JSON.parse(response.read_body)
tweet = nil

if parsedResponse["results"] == 0
    tweet = "No NBA games today"
else 
    tweet = "We have an NBA game today"
end
=end