package net.metadata.openannotation.lorestore.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openrdf.model.Literal;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResult;
import org.springframework.web.servlet.view.feed.AbstractAtomFeedView;

import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.atom.Person;

public class OREAtomFeedView extends AbstractAtomFeedView {

	@Override
	protected void buildFeedMetadata(Map<String, Object> model, Feed feed,
			HttpServletRequest request) {
		feed.setTitle("Compound Objects for " + (String)model.get("browseURL"));
//		feed.setId("link to reproduce this feed");
		
	}

	@Override
	protected List<Entry> buildFeedEntries(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<Entry> entries = new ArrayList<Entry>();
		
		TupleQueryResult results = (TupleQueryResult)model.get("queryResult");
		
		while (results.hasNext()) {
			BindingSet bs = results.next();
			Entry entry = new Entry();

			Literal l = (Literal)bs.getValue("m");
			entry.setModified(l.calendarValue().toGregorianCalendar().getTime());
			
			
			entry.setId(bs.getValue("g").stringValue());

			Person person = new Person();
			person.setName(bs.getValue("a").stringValue());
			entry.setAuthors(Collections.singletonList(person));
			
			entry.setTitle(bs.getValue("t").stringValue());
			
			entries.add(entry);
		}
		
		return entries;
	}

}
