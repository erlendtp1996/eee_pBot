from ohmysportsfeedspy import MySportsFeeds
import datetime
from os import environ

def getForDate():
    return datetime.datetime.today().strftime('%Y%m%d')

class MySportsFeedsFactory:
    def __init__( self, v = '1.2' ):
        self.msf = MySportsFeeds( version = v )
        self.msf.authenticate(environ['apikey_token'], environ['password'])

    def getMySportsFeed( self ):
        return self.msf

class DailySportsFetcher:
    def __init__( self ):
        sportsFeedFactory = MySportsFeedsFactory( '1.2' )
        self.msf = sportsFeedFactory.getMySportsFeed()
        self.date = getForDate()

    def fetch( self, feed ):
        return self.msf.msf_get_data( league='nba', season='2018-2019-regular', feed=feed, format='json', fordate=self.date )

    def getDailyGameList( self ):
        dailyGameList = []
        try:
            dailyGameJson = self.fetch( 'daily_game_schedule' )
            for game in dailyGameJson["dailygameschedule"]["gameentry"]:
                dailyGameList.append(str(game["homeTeam"]["Abbreviation"] + " vs " + game["awayTeam"]["Abbreviation"] + ": " + game["time"]))
        except Exception as e:
            print( e )
        return dailyGameList

    def getDate( self ):
        return self.date
