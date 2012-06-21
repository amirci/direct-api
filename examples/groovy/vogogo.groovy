#! /usr/bin/env groovy

@Grapes([
@Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.5.2')
])

entity = args.length ? args[0] : null

switch (entity) {
	case "create":
		data = args[1]
		println execute("post",
				"/api/payment/v1/invoice/",
				data,
				201)
		break
	case "get":
		id = args[1]
		execute("get", "/api/payment/v1/invoice/${id}/", null, 200)?.each { println it }
		break
	case "list":
		ids = args[1]
		execute("get", "/api/payment/v1/invoice/set/${ids}/", null, 200)['objects'].each { println "------"; it.each { println it }}
		break
	case "update":
		id = args[1]
		data = args[2]
		execute("patch",
				"/api/payment/v1/invoice/${id}/",
				data,
				202)?.each { println it }
		break
	case "delete":
		id = args[1]
		execute("delete", "/api/payment/v1/invoice/${id}/", null, 204)?.each { println it }
		break
	case "deleteAll":
		execute("delete", "/api/payment/v1/invoice/", null, 204)?.each { println it }
		break
	//
	// These cases are to demonstrate how to layout the actual HTTP body. They contain example data.
	//
	case "createInvoice":
		println execute("post",
				"/api/payment/v1/invoice/",
				[customer_ref: "Mr. Joe Jacob Blow",
						customer_emails: "joe@example.com",
						customer_contact_info: "123 Fake ST\nCalgary\nAlberta\n403.987.1231",
						profile_name: "Mega Mike's discount Golf Emporium",
						profile_emails: "snow@example.com",
						profile_contact_info: "403.123.1234",
						currency: "CAD",
						due_date: "2012-10-10",
						accept_cc: "True",
						accept_obp: "True",
						message: "This is your invoice for services rendered.",
						items: [[code: 'GOLFPINK2012', description: 'Dozen pink golf balls', unit_price: '24.99', quantity: '2'],
								[code: 'EXTREMECLUBS-M', description: '2012 Extreme Awesome Mens Golf Clubs', unit_price: '824.99', quantity: '1'],
								[description: 'Size club shafts', unit_price: '124.00', quantity: '2.5']]],
				201)
		break
	case "createRecurringInvoice":
		println execute("post",
				"/api/payment/v1/invoice/",
				[customer_ref: "Mr. Joe Jacob Blow",
						customer_emails: "joe@example.com",
						customer_contact_info: "123 Fake ST\nCalgary\nAlberta\n403.987.1231",
						profile_name: "Mega Mike's discount Golf Emporium",
						profile_emails: "snow@example.com",
						profile_contact_info: "403.123.1234",
						currency: "CAD",
						start_date: "2012-05-01",
						due_date: "2012-10-10",
						accept_cc: "True",
						accept_obp: "True",
						frequency: "MONTHLY",
						message: "This is your invoice for services rendered.",
						times: "12",
						items: [[code: '1403', description: 'Monthly rent for 403 Waterford Tower', unit_price: '2400.00', quantity: '1']]],
				201)
		break
	case "updateInvoice":
		invoiceId = args[1]
		execute("patch",
				"/api/payment/v1/invoice/${invoiceId}/",
				[customer_emails: "joe.blow@example.com"],
				202)?.each { println it }
		break
	default:
		println """
Usage:
	./vogogo.groovy create <data>
	./vogogo.groovy get <id>
	./vogogo.groovy update <id> <data>
	./vogogo.groovy delete <id>
	./vogogo.groovy deleteAll
		"""
		break
}

def execute(method, path, body, status) {
	def resp
	try {
		resp = new RESTClient('http://localhost:8000')."${method}"(path: path,
				query: [user: 'landlord@example.com', api_key: 'user_test_api_key', app_token: 'test_app_token'],
				requestContentType: groovyx.net.http.ContentType.JSON,
				body: body)
		assert resp.status == status
		m = resp.getLastHeader('Location') =~ /\/(\d*)\/$/
		m.size() > 0 ? m[0][1] : resp.data //Return the object id from the Location header or the response data.
	} catch (groovyx.net.http.HttpResponseException e) {
		println "${e.response?.status}" + "${e.response?.responseData ?: ''}"
	}
}

//Ignore this code. It only exists because HTTPBuilder and RESTClient don't support PATCH. This hacks it in.
public class HttpPatch extends org.apache.http.client.methods.HttpEntityEnclosingRequestBase {

	public final static String METHOD_NAME = "PATCH"

	public HttpPatch() { super() }

	public HttpPatch(final URI uri) {
		super()
		setURI(uri)
	}

	public HttpPatch(final String uri) {
		super();
		setURI(URI.create(uri))
	}

	@Override
	public String getMethod() {
		return METHOD_NAME
	}
}

public class RESTClient extends groovyx.net.http.RESTClient {

	public RESTClient() { super() }

	public RESTClient(Object defaultURI) throws URISyntaxException { super(defaultURI) }

	public RESTClient(Object defaultURI, Object defaultContentType) throws URISyntaxException { super(defaultURI, defaultContentType) }

	public Object patch(Map<String, ?> args) {
		return this.doRequest(new groovyx.net.http.HTTPBuilder.RequestConfigDelegate(this, args, new HttpPatch(), null))
	}
}
