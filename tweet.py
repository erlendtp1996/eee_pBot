import tweepy, random, time
from os import environ
from fetch.sportsDataFetcher import DailySportsFetcher

def initApi():
    consumer_key = environ['consumer_key']
    consumer_secret = environ['consumer_secret']
    access_token = environ['access_token']
    access_token_secret = environ['access_token_secret']
    auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token, access_token_secret)
    return tweepy.API(auth)

def listToTweet( stringList ):
    tweetList = []
    if stringList:
        tweet = ''
        for stringListItem in stringList:
            print ("StringListItem", stringListItem)
            if len( str(tweet + stringListItem + '\n')) < 140:
                tweet = str(tweet + stringListItem + '\n')
            else:
                tweetList.append(tweet)
                tweet = ''
        tweetList.append(tweet)
    return tweetList

twitterAPI = initApi()
dailyGameTwitterList = []

# runs for ten days
for i in range(0, 10):
    dsf = DailySportsFetcher()
    print ('Tweeting NBA GAMES FOR DATE = ' + str(dsf.getDate()))
    if not dailyGameTwitterList:
        dailyGameTwitterList = listToTweet( dsf.getDailyGameList() )
    for twt in dailyGameTwitterList:
        try:
            print ( 'Trying to tweet' )
            twitterAPI.update_status( twt )
            dailyGameTwitterList.remove( twt )
            print ( 'Tweeted: ' + str( twt ) )
        except:
            print ( 'ERROR Tweeting' )
    time.sleep(60 * 60 * 24)
