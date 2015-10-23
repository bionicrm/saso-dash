package io.saso.dash.templating

import io.saso.dash.util.Resources

enum Template
{
    GITHUB_COMMIT_TEMPLATE('github/commit'),
    GITHUB_ISSUE_TEMPLATE('github/issue'),
    GITHUB_PULL_REQUEST_TEMPLATE('github/pull_request')

    final String path

    private Template(String path)
    {
        this.path = "templates/${path}.twig"
    }

    /**
     * Fetches the contents of the template.
     *
     * @return the contents of the template
     */
    String fetch()
    {
        Resources.get path
    }
}
