package com.eeepbot.bots;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.json.JSONObject;

/**
 * request: query { 
user(login:"erlendtp1996") { 
contributionsCollection (from:"2022-04-24T16:27:55Z") {
  totalCommitContributions
}
}
}
 *  response:
 *  {
"data": {
"user": {
  "contributionsCollection": {
    "totalCommitContributions": 4
  }
}
}
}
 */
public class GithubTwitterBot extends TwitterBot {
	
	private final String GITHUB_GRAPHQL_ENDPOINT = "https://api.github.com/graphql";
	private URL url;
	private HttpURLConnection connection;
	
	private String githubToken;
	private String requestBody;
	
	public GithubTwitterBot() {
		super();
	}
	
	public void setProperties(Map<String, String> props) {
		setGithubToken(props.get("githubToken"));
		super.setProperties(props);
	}

	private String wrap(String str, String wrapper) {
		return wrapper + str + wrapper;
	}
	
	private String wrap(String beginning, String str, String end) {
		return beginning + str + end;
	}
	
	private String wrapString(String str) {
		return wrap(str, "\"");
	}
	
	private String wrapParams(String str) {
		return wrap(str, "\\\"");
	}
	
	private String buildGraphQLRequest() {
		StringBuilder str = new StringBuilder();
		str.append("query { ");
		str.append("user(login:" + wrapParams("erlendtp1996") + ") { ");
		str.append("contributionsCollection(from:" + wrapParams("2022-04-24T16:27:55Z") + ") { ");
		str.append("totalCommitContributions ");
		str.append("} ");
		str.append("} ");
		str.append("}");
		return str.toString();
	}
	
	private String buildRequestBody() {
		StringBuilder str = new StringBuilder();
		str.append("{ ");
		str.append(wrapString("query"));
		str.append(": ");
		str.append(wrapString(buildGraphQLRequest()));
		str.append(" }");
		return str.toString();
	}	

	@Override
	public void generateTweetList() {
		try {
			/*requestBody = buildRequestBody();
			
			url = new URL(GITHUB_GRAPHQL_ENDPOINT);
			connection = (HttpURLConnection) url.openConnection();
			
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Length", Integer.toString(requestBody.getBytes().length));
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", "Bearer " + githubToken);
			connection.setDoOutput(true);
			
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(requestBody);
			wr.close();
			
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append("\r");
			}
			rd.close();
			
			String jsonString = response.toString() ; //assign your JSON String here
			JSONObject obj = new JSONObject(jsonString);
			Integer commits = obj.getJSONObject("data")
					.getJSONObject("user")
					.getJSONObject("contributionsCollection").getInt("totalCommitContributions");

			System.out.println("commits: " + commits.toString());*/
			
			setTweets("the service thread tweeting this was generated dynamically");
		}
		catch (Exception e) {
			System.out.println(e);
			
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	
	public void setGithubToken(String githubToken) {
		this.githubToken = githubToken;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}
}
