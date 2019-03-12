import tweepy, random, time
from dailyGames import getDailyGameTweets
from os import environ

def initApi():
    consumer_key = environ['consumer_key']
    consumer_secret = environ['consumer_secret']
    access_token = environ['access_token']
    access_token_secret = environ['access_token_secret']
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
dailyGameTwtterList = []

for i in range(0, 10):
    if not dailyGameTwtterList:
        dailyGameTwitterList = generateTweets( getDailyGameTweets() )
    for twt in dailyGameTwitterList:
        try:
            print ( 'Trying to tweet' )
            twitterAPI.update_status( twt )
            dailyGameTwtterList.remove( twt )
            print ( 'Tweeted: ' + str( twt ) )
        except:
            print ( 'ERROR Tweeting' )
    time.sleep(60 * 60 * 24)
