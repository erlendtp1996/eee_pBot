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

    def fetchJSON( self, feed, league, season ):
        return self.msf.msf_get_data( league=league, season=season, feed=feed, format='json', fordate=self.date);

    def fetchNBA( self, feed, season ):
        return self.fetchJSON( feed, 'nba', season );

    def fetchMLB( self, feed, season );
        return self.fetchJSON( feed, 'mlb', season );

    def getDailyGameList( self ):
        dailyGameList = []
        try:
            dailyGameJson = self.fetchMLB( 'daily_game_schedule', '2019-regular' )
            if ( 'gameentry' in dailyGameJson['dailygameschedule'] ):
                for game in dailyGameJson['dailygameschedule']['gameentry']:
                    dailyGameList.append(str(game['homeTeam']['Abbreviation'] + " vs " + game['awayTeam']['Abbreviation'] + ": " + game['time']))
            else:
                dailyGameList.append(" No games today! SAD! ;( ")
        except Exception as e:
            print( "Exception occured", e )
            dailyGameList.append( 'There was an error tweeting the games!' )
        return dailyGameList

    def getDate( self ):
        return self.date
