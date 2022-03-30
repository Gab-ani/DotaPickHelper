import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class dataFetcher {    	// utility class used to get somewhere the data about heroes
								// currently uses html parse of dotabuff
	
	public static HashMap<String, Double> fetchAdvantageTable(String url) throws IOException, ParseException {
		HashMap<String, Double> advantageResults = new HashMap<String, Double>();
		Document doc = Jsoup.connect(url).get();
		Elements links = doc.select("tr");
		for (Element link : links) {
			String value = link.attr("data-link-to");
			if(value.contains("heroes")) {
				Elements e = link.select("td");
				String name = Model.toDBNamingRules(e.get(1).text());
				String advantage = e.get(2).text();
				advantage  = advantage.replace('%', ' ');
				advantageResults.put(name, Double.parseDouble(advantage) * -1);			// dotabuff uses term "disadvantage", and we use "advantage", so multiplying by -1
			}
		}
        return advantageResults;
	}
	
}
