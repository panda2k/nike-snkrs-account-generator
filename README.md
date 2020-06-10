# nike-snkrs-account-generator
Uses selenium to create Nike SNKRS, Protonmail, and Gmail accounts. This program uses getsmscode.com to get phone number verification texts for all of the generated accounts. 


## Disclaimer
Before spending money on getsmscode credits, realize that this generator may not currently work since it is not under active development. This generator also doesn't generate account activity on the SNKRS accounts so it is likely that they get filtered during releases. 


## Installation
Clone the repository with Github's web interface or with the command line. `git clone https://github.com/panda2k/nike-snkrs-account-generator.git`


In order for the phone verification to work, you'll have to create an account at getsmscode.com. Next, set the variables `username` and `token` equal to your getsmscode username and API token. Instructions on how to find that can be found here: https://www.getsmscode.com/api.html


Make sure you have balance on your account before you start. 


This program also uses Maven to manage dependencies. Install maven by following the intructions here: https://maven.apache.org/install.html


## Usage
Run the program using maven when in the project directory. 

`mvn exec:java`


Maven will install dependencies before it runs. 


Follow the command line prompts to create Nike SNKRS accounts. If you choose not to use real emails, the nike accounts will be made with fake Gmails meaning that if you're account needs a password reset, you won't be able to reset the password. By default, the generated emails are protonmail.


After the program is done generating the accounts, it will write them to your desired text file. 


## Known Issues
Gmail accounts aren't properly generated and if you manually finish Gmail account generation, the accounts normally start disabled and you will need to undisable the accounts. 
