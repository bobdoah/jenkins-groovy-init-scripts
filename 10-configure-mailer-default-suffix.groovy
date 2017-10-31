import jenkins.model.*

inst = Jenkins.getInstance()
def desc = inst.getDescriptor("hudson.tasks.Mailer")
defaultSuffix = System.getenv("EMAIL_SUFFIX") ?: "@blah"
desc.setDefaultSuffix(defaultSuffix)
desc.save()
