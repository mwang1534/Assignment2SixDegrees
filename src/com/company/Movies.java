package com.company;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;
import java.util.ArrayList;
import java.util.Queue;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
/*
 * Downloaded JAR files from:
 *   http://commons.apache.org/proper/commons-csv/user-guide.html (Apache Commons CSV)
 *   http://www.java2s.com/Code/Jar/j/Downloadjsonsimple11jar.htm (JSON-Simple)
 *
 * Got them in my build path using:
 *   https://www.wikihow.com/Add-JARs-to-Project-Build-Paths-in-Eclipse-%28Java%29
 */

public class Movies {

	public int bfs (Hashtable<String, String> actormovie, String name1, String name2) {
		int degrees = 0;
		String pop; //for storing the next person in potential path
		ArrayList<String> path = new ArrayList<String>();
		boolean [] visited = new boolean[actormovie.size()];

		Queue<String> q = new LinkedList<String>();
		visited[actormovie.get(name1).hashCode()] = true;
		q.add(name1); //queues actor
		path.add(name1); //adds actor to potential path

		while (!q.isEmpty()) { //will work while queue is not empty
			q.remove(name1); //removes name from queue after visited
			for (int adj : incident(name1)) { //checks each person related
				if(!visited[adj]) {
					q.add(adj); //adds related people to queue
					visited[adj] = true; //marks related people as visited
				}
			}
		}
		return degrees;
	}

	public static Hashtable<String, String> moviecast() {
		try {
			Reader reader = new FileReader("tmdb_5000_credits.csv");
			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
			JSONParser jsonParser = new JSONParser();

			int movies = 0;

			for (CSVRecord csvRecord : csvParser) {
				if (movies >= 1) {
					String title = csvRecord.get(1);
					String castJSON = csvRecord.get(2);
					Hashtable <String, String> moviecast = new Hashtable<>();
					// [] = array
					// {} = "object" / "dictionary" / "hashtable" -- key "name": value

					System.out.println("Title: " + title);
					Object object = jsonParser.parse(castJSON);

					JSONArray jsonArray = (JSONArray)object;

					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject jsonObject = (JSONObject)jsonArray.get(i); //for listing each actor
						System.out.println(" * " + jsonObject.get("name")); //for listing each actor
						moviecast.put((String) jsonArray.get(i), title); //adds actor(key) + movie(value)
					}
					System.out.println(moviecast); //for debugging
					return moviecast;
				}
				++movies;
			}

			csvParser.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("File is invalid or is in the wrong format.");
			System.out.println("Actor/Actress may not exist.");
		}
	}

	public static void main (String[] args) {
		Hashtable <String, String> moviecast = moviecast();

		System.out.println("Please enter an Actor/Actress");
		Scanner name1 = new Scanner(System.in);
		System.out.println("Please enter a second Actor/Actress");
		Scanner name2 = new Scanner(System.in);

		if (moviecast.get(name1) == moviecast.get(name2)) {
			System.out.println("These two actors have been in the same movie together.");
			System.out.println("Path between " + name1 + " and " + name2);
		}
	}
}