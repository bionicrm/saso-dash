package io.saso.dash.services

import com.google.inject.name.Named
import org.kohsuke.github.GitHub

public interface SubServiceFactory
{
    @Named("github-notification")
    fun createGitHubNotificationSubService(gitHub: GitHub): ServicePollable
}
