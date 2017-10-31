import jenkins.model.*
import org.apache.commons.lang.StringUtils
import org.jfrog.hudson.*

def artifactoryHostname = System.getenv("ARTIFACTORY_HOSTNAME")
def artifactoryUrl = System.getenv("ARTIFACTORY_URL")
if (artifactoryHostname == null && artifactoryUrl == null){
    println("No ARTIFACTORY_HOSTNAME or ARTIFACTORY_URL env var provided.")
    println("--> Skipping Artifactory plugin configuration")
    return
}

println("Configuring Artifactory instance: ${artifactoryUrl ?: artifactoryHostname}")

def artifactoryUsername = System.getenv("ARTIFACTORY_USERNAME")
assert artifactoryUsername != null :"No ARTIFACTORY_USERNAME env var provided, but it is required"

def artifactoryPassword = System.getenv("ARTIFACTORY_PASSWORD")
assert artifactoryPassword != null :"No ARTIFACTORY_PASSWORD env var provided, but it is required"

def artifactoryPort = System.getenv("ARTIFACTORY_PORT")
artifactoryPort = artifactoryPort != null ? artifactoryPort.toInteger() : 8081

def artifactoryTimeout = System.getenv("ARTIFACTORY_TIMEOUT")
artifactoryTimeout = artifactoryTimeout != null ? artifactoryTimeout.toInteger() : 300

def inst = Jenkins.getInstance()

def desc = inst.getDescriptor("org.jfrog.hudson.ArtifactoryBuilder")

def deployerCredentialsConfig = new CredentialsConfig(artifactoryUsername, artifactoryPassword, StringUtils.EMPTY, false)

artifactoryBypassProxyDefault = System.getenv('http_proxy') != null // If we don't use a proxy, we don't need to set this

def artifactoryBypassProxy = System.getenv("ARTIFACTORY_BYPASS_PROXY")
if (artifactoryBypassProxy == null){
    println("--> No ARTIFACTORY_BYPASS_PROXY setting, assuming default (${artifactoryBypassProxyDefault})")
    artifactoryBypassProxy = artifactoryBypassProxyDefault
} else {
  artifactoryBypassProxy = artifactoryBypassProxy.toBoolean()
}

def sinst = [new ArtifactoryServer(
    System.getenv("ARTIFACTORY_NAME") ?: "artifactory",
    artifactoryUrl ?: "http://${artifactoryHostname}:${artifactoryPort}/artifactory".toString(),
    deployerCredentialsConfig,
    null,
    artifactoryTimeout,
    artifactoryBypassProxy ) // Bypass the proxy
]

desc.setArtifactoryServers(sinst)
desc.save()
