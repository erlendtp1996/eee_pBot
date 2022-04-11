import tweepy, random, time
from os import environ
from sportsDataFetcher import *

"""
uninstall pip
uninstall python 2
uninstall python 3
install python 3
install pip

make sure pip points to the correct python

access_token - 982034714339966976-oYm8IIvchpMVLrumO4vRg7B9K9aQM4J
access_token_secret - LIFSKVs2UtrrD2kCPB5PU5QlUKjy4R8k0a7lZxE6aR5fJ
apikey_token - c1bb6ebc-a473-4626-832b-de4db6
consumer_key - l2qNQISzGtFhnznDOI6iJi8ld
consumer_secret - RAiE6GKtWvxXG4ei8AZ00q6RKxVnQnKHiFRvMExsuJX64rezmw
password - nbaAPI1!

consumer_key = environ['consumer_key']
consumer_secret = environ['consumer_secret']
access_token = environ['access_token']
access_token_secret = environ['access_token_secret']

"""

def initApi():
    consumer_key = 'l2qNQISzGtFhnznDOI6iJi8ld'
    consumer_secret = 'RAiE6GKtWvxXG4ei8AZ00q6RKxVnQnKHiFRvMExsuJX64rezmw'
    access_token = '982034714339966976-oYm8IIvchpMVLrumO4vRg7B9K9aQM4J'
    access_token_secret = 'LIFSKVs2UtrrD2kCPB5PU5QlUKjy4R8k0a7lZxE6aR5fJ'
    auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token, access_token_secret)
    return tweepy.API(auth)

def initLeagueList():
    leagueList = []
    leagueList.append(MySportsFeedLeague('nba', 'NBA'))
    leagueList.append(MySportsFeedLeague('mlb', 'MLB'))
    leagueList.append(MySportsFeedLeague('nfl', 'NFL'))
    return leagueList

def listToTweet( stringList, title ):
    tweetList = []
    if stringList:
        tweet = title + ':\n'
        for stringListItem in stringList:
            if len( str(tweet + stringListItem + '\n')) < 140:
                tweet = str(tweet + stringListItem + '\n')
            else:
                tweetList.append(tweet)
                tweet = ''
        tweetList.append(tweet)
    return tweetList

def tweetLeague( dsf, league ):
    json = dsf.fetchLeague( 'daily_game_schedule', league.getName(), '2019-2020-regular' )
    dailyGameList = dsf.getDailyGameList( json, league.getTitle() )
    dailyGameTwitterList = listToTweet( dailyGameList, league.getTitle() )
    for twt in dailyGameTwitterList:
        try:
            print ( 'Trying to tweet' )
            twitterAPI.update_status( twt )
            dailyGameTwitterList.remove( twt )
            print ( 'Tweeted: ' + str( twt ) )
        except:
            print ( 'ERROR Tweeting' )

"""

Begin logic
    - load api
    - load leagueList
    - tweet every league

"""

twitterAPI = initApi()
leagueList = initLeagueList()

dsf = DailySportsFetcher()
print ('Tweeting GAMES FOR DATE = ' + str(dsf.getDate()))
for league in leagueList:
    tweetLeague( dsf, league )
