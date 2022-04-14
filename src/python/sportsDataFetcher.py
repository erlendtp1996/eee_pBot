from ohmysportsfeedspy import MySportsFeeds
import datetime
from os import environ

def currDate():
    return datetime.datetime.today().strftime('%Y%m%d')

"""
    League
"""
class MySportsFeedLeague:
    def __init__( self, name, title ):
        self.name = name
        self.title = title

    def getName( self ):
        return self.name

    def getTitle( self ):
        return self.title

"""
    League
"""
class MySportsFeedsFactory:
    def __init__( self, v = '1.2' ):
        self.msf = MySportsFeeds( version = v )
        #self.msf.authenticate(environ['apikey_token'], environ['password'])

    def getMySportsFeed( self ):
        return self.msf

"""
    League
"""
class DailySportsFetcher:
    def __init__( self ):
        sportsFeedFactory = MySportsFeedsFactory( '1.2' )
        self.msf = sportsFeedFactory.getMySportsFeed()

    def fetchJSON( self, feed, league, season ):
        date = currDate()
        print (feed, league, season, date);
        return self.msf.msf_get_data( league=league, season=season, feed=feed, format='json', fordate=date);

    def fetchLeague( self, feed, league, season ):
        return self.fetchJSON( feed, league, season );

    def getDailyGameList( self, dailyGameJson, title ):
        dailyGameList = []
        try:
            if ( 'gameentry' in dailyGameJson['dailygameschedule'] ):
                for game in dailyGameJson['dailygameschedule']['gameentry']:
                    dailyGameList.append(str(game['homeTeam']['Abbreviation'] + " vs " + game['awayTeam']['Abbreviation'] + ": " + game['time']))
            else:
                dailyGameList.append("No " + title + " games today! SAD! ;( ")
        except Exception as e:
            print( "Exception occured", e )
            dailyGameList.append( 'There was an error tweeting the games!' )
        return dailyGameList

    def getDate( self ):
        return currDate()
