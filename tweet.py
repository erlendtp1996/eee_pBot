import tweepy, random, time
from dailyGames import getDailyGameTweets

def initApi():
    keyFile = open('keys.txt', 'r')
    keys = keyFile.readlines();
    keyFile.close()
    consumer_key = keys[0]
    consumer_secret = keys[1]
    access_token = keys[2]
    access_token_secret = keys[3]
    auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token, access_token_secret)
    return tweepy.API(auth)

def getTweets():
    tweetFile = open('tweets.txt', 'r')
    tweets = tweetFile.readlines()
    tweetFile.close()
    return tweets

def generateTweets( dailyGames ):
    if dailyGames == False:
        return "Error generating the games today!"
    else:
        tweet = ""
        tweetList = []
        for dailyGame in dailyGames:
            if len( tweet + dailyGame + "\n") < 140:
                tweet = tweet + dailyGame + "\n"
            else:
                tweetList.append(tweet)
                tweet = ""
        tweetList.append(tweet)
        return tweetList

twitterAPI = initApi()
dailyGameTwitterList = generateTweets( getDailyGameTweets() )

for i in xrange(0, 10):
    for twt in dailyGameTwitterList:
        print twt
        twitterAPI.update_status(twt)
    time.sleep(60 * 60 * 24)
