Vogogo Direct Integration API
==========

Api documentation and examples used to interact with vogogo.com

RESTful API
==========
----
The RESTful API interacts with Vogogo via https://api.vogogo.com/api/payment/v1/invoice/ via HTTP GET, POST, PATCH and DELETE.

* GET: used to return the details of an invoice.
* POST: used to create a new invoice.
* PATCH: used to update an existing invoice.
* DELETE: used to delete a single or all invoices.

Below is an example of an invoice being created using [curl](http://curl.haxx.se/ "Curl").

    curl --dump-header - -H "Content-Type: application/json" -X POST --data '{"number": "1234", "customer_ref": "Mr. Joe Jacob Blow", "customer_emails": "joe@example.com", "customer_contact_info": "123 Fake ST\nCalgary\nAlberta\n403.987.1231", "profile_name": "Jims Snow Removal", "profile_emails": "snow@example.com", "profile_contact_info": "403.123.1234", "po": "1234",  "currency": "CAD", "accept_cc": "True", "accept_obp": "True", "due_date": "2012-10-10", "message": "This is your invoice for services rendered.", "paid": "False", "items": [{"code": "GOLFPINK2012", "description": "Dozen pink golf balls", "unit_price": "24.99", "quantity": "2"},{"code": "EXTREMECLUBS-M", "description": "2012 Extreme Awesome Mens Golf Clubs", "unit_price": "824.99", "quantity": "1"},{"description": "Size club shafts", "unit_price": "124.00", "quantity": "2.5", "tax":"GST"}]}' https://api.vogogo.com/api/payment/v1/invoice/?user=landlord@example.com\&api_key=user_test_api_key\&app_token=test_app_token

curl is setting the Content-Type to application/json and performing a POST with the body of the POST containing a JSON collection of name/value pairs. The last argument to curl is the URL in which the data should be posted. In the above example the URL has three parameters attached, these are your authentication credentials. It is also possible to pass them as HTTP Headers.

Individual parameters are defined below. For more examples please see the examples in this project.

Authentication 
==========
Authentication is performed by passing the following three arguments along with every operation. They can either be passed as query string parameters or as HTTP headers. They should not be passed in the body of the JSON dictionary.

Parameter         | Description       
----------------- | -----------------
**user**          | Contains the email address of the user responsible for submitting this request. In many cases you will want to create a dedicated user in your Vogogo account in order to separate human users from API users.|
**api_key**       | Contains a key that uniquely identifies your organization in Vogogo. This is not the password of the user provided in the user argument but it should be treated with the same level of care. It should not be shared with anybody.|
**app_token**     | Contains a token used to uniquely identify different applications that are authorized to interact with your Vogogo account. This is provided as a separate token in order to allow disable individual applications for interacting with your Vogogo account. This token is provided by you and should be unique of each application.|

Invoice Parameters
==========
In addition to the authentication parameters provided above the following parameters are used to provide details on how Vogogo should create a new invoice.

**Parameter**                 | **Description** | **Required** 
----------------------------- | --------------- | ------------
**number**                    | Your organizations invoice number. If one is not provided they will be created starting with invoice #1.|False
**customer_ref**              | This is typically the name of your customer.|False
**customer_emails**           | A list of space or comma separated emails addresses in which the invoice should be sent.|False
**customer_contact_info**     | Any additional contact information you want to display on the invoice. Typically their address.|False
**profile_name**              | The name of your organization. Typically a operating or doing business as name.|False
**profile_emails**            | A list of space or comma separated emails you want as the reply-to field on the outgoing invoice email.|False
**profile_contact_info**      | Additional contact information you want to display on the invoice.|False
**po**                        |Typically a PO number. | False
**currency**                  | The currency in which the invoice will be displayed. Valid options are USD and CAD.|False
**estimate**                  | A boolean flag signifying an estimate should be created and sent opposed to an invoice.|False
**accept_pad**                | |False
**accept_cc**                 | |False
**accept_obp**                | |False
**start_date**                |  |False
**due_date**                  | The date in which a customer is supposed to have completed payment of this invoice. Format YYYY-MM-DD. Example:2012-07-31|False
**frequency**                 | Invoice will be recurring at the following intervals starting on the provided start_date. Valid options are ONETIME, WEEKLY, BIWEEKLY, MONTHLY. If omitted ONETIME is assumed.|False|
**message**                   | |False
**times**                     | |False
**items**                     | A JSON list of dictionaries containing at least 1 item to display on the invoive. See Invoice Item Parameters below for more information.|True


Invoice Item Parameters
==========
An invoice can have many items.

**Parameter**   | **Description** | **Required** 
--------------- | --------------- | ------------
**code**        | A product code or SKU you want to display for this item.|False
**description** | Product or service description you want to display for this item.|False
**unit_price**  | The price of this item. Note: The currency is established by the currency parameter on the invoice. An invoice can only be issued with one currency.|True
**quantity**    | The number of items being sold. This can either be a decimal or an integer.|True
**tax**         | The name of the tax to apply. Taxes are configured within Vogogo. If the provided value doesn't match the name (not short_name) of a configured tax the API call will fail. The short_name of the provided tax is displayed on the invoice.|False


Additional Response Fields
==========
The following fields are returned in addition to the other parameters when requesting the details of an invoice. They are calculated by the system.

**Field**        | **Description**
---------------- | --------------
**id**           | The id of this Invoice.
**issued_date**  | The date this invoice was sent to the customer.
**status**       | That current status of this invoice. Please see Invoice Status below for more information.
**paid_date**    | The date in which this invoice was paid.
**amount**       | The total amount of this invoice including tax.
**due**          | The amount outstanding on this invoice.
**url**          | A url which will be sent to the customer in order to collect payment.


Invoice Status
==========
The following status codes are returned when viewing an existing invoice.

**Status**      | **Description**
--------------- | ---------------
**UNSENT**      | Invoice has been created but not yet sent to the customer.|
**SENT**        | Invoice has been sent to the customer but it has not yet been paid.|
**OVERDUE**     | Invoice has not been paid by end of day on the invoice due_date.|
**CLOSED**      | Account holder has decided no further account is required. i.e. invoice was paid with cash or cheque.|
**PENDING**     | Only occurs are pre-authorized debit requests while the system is waiting for the payment to be cleared.|
**PAID**        | Invoice has been paid and the funds have cleared.|
**FAILED**      | Payment failed to clear for this invoice.|


Response Codes
==========
Response codes returned are standard HTTP codes based on the operation being performed. For example successfully creating an invoice will return a [201](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.2 "HTTP 201"). Please see the examples in this project for more details.
