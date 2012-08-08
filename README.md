Test service
============

Build
------
Need Apache Maven. Execute

    mvn package -DskipTests 

That produces two tarballs under target/releases, one for the service, one for the client.

Install
-------
Untar the service tarball in a directory of your choice

    tar -xzvf target/releases/test-0.0.1-SNAPSHOT-service.tar.gz

Run 
----

The service needs a database. Create one than edit config/service.properties to provide the database connection url,  username and password.

The service needs an host certificate for doing HTTPS. It must be in /etc/grid-security/hostcert.pem with the private key in /etc/grid-security/hostkey.pem. 
The service looks for trusted CAs under /etc/grid-security/certificates.

By default the service logs on standard output. That got to be a way to make it log to file but it does not comes to my mind now. Sorry.

Execute

    ./bin/service
    
to start the service.

API
---

### Delegating credential to the service

Delegating credentials to the service means making available to the service a proxy certificate originated by the user's end entity certificate.

To delegate credentials to the service a user initialize the process by doing a POST request on /delegation without contents in the body.
<pre>
POST /delegation
</pre>

The service returns a 201 Created Response with CSR in PEM format in the body

<pre>
201 Created
Content-Type: text/plain

-----BEGIN CERTIFICATE REQUEST-----
MIIBtDCCAR0CAQAwdDELMAkGA1UEBhMCSVQxDTALBgNVBAoTBElORk4xHTAbBgNV
BAsTFFBlcnNvbmFsIENlcnRpZmljYXRlMQ0wCwYDVQQHEwRDTkFGMRgwFgYDVQQD
Ew9WYWxlcmlvIFZlbnR1cmkxDjAMBgNVBAMTBTIyMDkwMIGfMA0GCSqGSIb3DQEB
AQUAA4GNADCBiQKBgQDGL4TTr873vkxnWdtgh4nyIJy0PbGqx0IANUVR7bZGfKcZ
qaRfhdQVcaUmRDO2iHqI6A7mLy1RA5POQi+KBdMZgyBFKxPaIeqkb2qRmSEIXD0h
yWs1hHBHOI0q0dfgaNgtZUkk7OcuA2873NwK8Rq8qhd2Ps1rJEt9cPQ2FOKY+wID
AQABoAAwDQYJKoZIhvcNAQEFBQADgYEASReXcn5RruKBaYG8soNPLaw+KKzvurmj
6qA/C9RdFWFmCcLJL3AtNiKtHl05oWibCo1CJy9N5mjn2W4zl1QtAvLrkVNu8bwm
MBlH2N0wvpz/hAAq0wwdJmwnzsfdZFJ9uG/ee8gw8fVA9vOafbm9hfCHDl6JhlHd
0o/RxaZrxiI=
-----END CERTIFICATE REQUEST-----
</pre>

or a 500 Internal Server Error if something went wrong.

The user signs the CSR using the certificate with which he authenticated, and send the resulting 
proxy certificare to the service doing a PUT request on /delegation with the certificate, using the Content-Type
plain/text, and putting the certificate in PEM format in the body

<pre>
PUT /delegation
Content-Type: text/plain

MIICkDCCAfmgAwIBAgICVkowDQYJKoZIhvcNAQEFBQAwZDELMAkGA1UEBhMCSVQx
DTALBgNVBAoTBElORk4xHTAbBgNVBAsTFFBlcnNvbmFsIENlcnRpZmljYXRlMQ0w
CwYDVQQHEwRDTkFGMRgwFgYDVQQDEw9WYWxlcmlvIFZlbnR1cmkwHhcNMTIwODA3
MTIxMjM3WhcNMTIwODA4MDAxNzM3WjB0MQswCQYDVQQGEwJJVDENMAsGA1UEChME
SU5GTjEdMBsGA1UECxMUUGVyc29uYWwgQ2VydGlmaWNhdGUxDTALBgNVBAcTBENO
QUYxGDAWBgNVBAMTD1ZhbGVyaW8gVmVudHVyaTEOMAwGA1UEAxMFcHJveHkwgZ8w
DQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAKR5ZedLjVqqzG/JZ71aNT6dkIyYDDFo
GPbYAKht+uXxbK98M0vmBgeppgJFbHOHmZmA0My6txrEkr3jA/JFm8fdvcjpZFEJ
5Z7fKG7ZrxEW4sXjIpnsaleaJjIRf1174IxuQyrjw6rPTd5M327TmGlJt7o0/Cg7
pZ0AnHBvhl8dAgMBAAGjQTA/MA4GA1UdDwEB/wQEAwIEsDAMBgNVHRMBAf8EAjAA
MB8GA1UdIwQYMBaAFOz08kn7VmL1V1V9SNKXJBVBxy2qMA0GCSqGSIb3DQEBBQUA
A4GBAAZwH1iiY9OS1Pi8xYe2iml/6yjg4naY5ek8l2wuGZP1g42yCr3czVJczmU9
y/CpND4DIMfe8XCPmEbw8IdpozK9cRjLFcEwSPWHYten9cx79NipciqSJXab4QHW
gNO/x0f2hA5BNYNIadvuINK+noZlWtG3ZvIjFbg9Vhdcjmsg
</pre>

The service returns a 200 OK response without content in the body

<pre>
200 OK
</pre>

Command line
------------

Untar the client tarball in a directory of your choice

    tar -xzvf target/releases/test-0.0.1-SNAPSHOT-service.tar.gz

Edit config/client.properties providing the service base url.

You must have a proxy certificate in the usual tmp/x509up_u<uid>. Then execute

### Delegating credentials to the service

Execute

    ./bin/delegate
