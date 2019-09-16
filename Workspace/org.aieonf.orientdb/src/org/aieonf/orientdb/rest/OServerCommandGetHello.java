package org.aieonf.orientdb.rest;

import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.orientechnologies.orient.server.config.OServerEntryConfiguration;
import com.orientechnologies.orient.server.network.protocol.http.OHttpRequest;
import com.orientechnologies.orient.server.network.protocol.http.OHttpResponse;
import com.orientechnologies.orient.server.network.protocol.http.OHttpUtils;
import com.orientechnologies.orient.server.network.protocol.http.command.OServerCommandAuthenticatedDbAbstract;

public class OServerCommandGetHello extends OServerCommandAuthenticatedDbAbstract {

	public OServerCommandGetHello() {
		// TODO Auto-generated constructor stub
	}

	// DECLARE THE PARAMETERS
	private boolean italic = false;

	public OServerCommandGetHello(final OServerCommandConfiguration iConfiguration) {
		// PARSE PARAMETERS ON STARTUP
		for (OServerEntryConfiguration par : iConfiguration.parameters) {
			if (par.name.equals("italic")) {
				italic = Boolean.parseBoolean(par.value);
			}
		}
	}

	@Override
	public boolean execute(final OHttpRequest iRequest, OHttpResponse iResponse) throws Exception {
		// CHECK THE SYNTAX. 3 IS THE NUMBER OF MANDATORY PARAMETERS
		String[] urlParts = checkSyntax(iRequest.url, 3, "Syntax error: hello/<database>/<name>");

		// TELLS TO THE SERVER WHAT I'M DOING (IT'S FOR THE PROFILER)
		iRequest.data.commandInfo = "Salutation";
		iRequest.data.commandDetail = "This is just a test";

		// GET THE PARAMETERS
		String name = urlParts[2];

		// CREATE THE RESULT
		String result = "Hello " + name;
		if (italic) {
			result = "<i>" + result + "</i>";
		}

		// SEND BACK THE RESPONSE AS TEXT
		iResponse.send(OHttpUtils.STATUS_OK_CODE, "OK", null, OHttpUtils.CONTENT_TEXT_PLAIN, result);

		// RETURN ALWAYS FALSE, UNLESS YOU WANT TO EXECUTE COMMANDS IN CHAIN
		return false;
	}

	@Override
	public String[] getNames() {
		return new String[]{"GET|hello/* POST|hello/*"};
	}
}
