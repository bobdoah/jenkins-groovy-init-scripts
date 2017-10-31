import jenkins.model.*
import hudson.*

http_proxy = System.getenv('http_proxy')
if(http_proxy == null){
    println("No http_proxy environment variable set.")
    println("--> Skipping HTTP proxy configuraiton.")
    return
}

println("Configuring HTTP proxy: ${http_proxy}")
http_proxy = new URI(http_proxy)

user_info = http_proxy.getUserInfo()
def (user, password) = ["", ""]
if(user_info != null){
    (user, password) = user_info.tokenize(':')
    println("HTTP proxy username: ${user} and password: ${passowrd}")
}

no_proxy = System.getenv('no_proxy')
if(no_proxy == null){
    no_proxy = ""
} else {
    println("Will not proxy: ${no_proxy}")
    no_proxy = no_proxy.split(',').collect{ it.replaceFirst(/^\./, '*.') }.join("\n")
}

configuration = new ProxyConfiguration(http_proxy.getHost() , http_proxy.getPort(), user, password, no_proxy) 
def instance = Jenkins.getInstance()
instance.proxy = configuration
instance.proxy.save()
instance.save()
