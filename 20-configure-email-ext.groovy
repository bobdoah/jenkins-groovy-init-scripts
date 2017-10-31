import jenkins.model.*

inst = Jenkins.getInstance()

def desc = inst.getDescriptor("hudson.plugins.emailext.ExtendedEmailPublisher")
smtpHost = System.getenv("SMTP_HOST")

println("Configuring SMTP host ${smtpHost}")

desc.smtpHost = smtpHost
desc.smtpAuthUsername = System.getenv("SMTP_AUTH_USERNAME")
desc.smtpAuthPassword = System.getenv("SMTP_AUTH_PASSWORD")

desc.defaultPresendScript = '''
File sourceFile = new File("/var/jenkins_home/groovy-scripts/resolveRecipients.groovy")
Class ResolveRecipients = new GroovyClassLoader(getClass().getClassLoader()).parseClass(sourceFile);
def resolveRecipients = ResolveRecipients.newInstance()
resolveRecipients.validateRecipients(msg, logger, build, cancel)
'''
desc.defaultBody = '${JELLY_SCRIPT,template="html"}'
desc.defaultContentType = 'text/html'
desc.precedenceBulk = true
desc.save()


