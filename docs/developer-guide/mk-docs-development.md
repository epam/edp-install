# Documentation Flow

This section defines necessary steps to start developing the EDP documentation in the MkDocs Framework.
The framework presents a static site generator with documentation written in Markdown. All the docs are configured with
a YAML configuration file.

!!! note
    For more details on the framework, please refer to the [MkDocs official website](https://www.mkdocs.org/).

There are two options for working with MkDocs:

* Work with MkDocs if Docker is installed
* Work with MkDocs if Docker is not installed

Please see below the detailed description of each options and choose the one that suits you.

## MkDocs With Docker

Prerequisites:

* Docker is installed.

* ````make```` utility is installed.

* Git is installed. Please refer to the [Git downloads](https://git-scm.com/downloads).

To work with MkDocs, take the following steps:

1. Clone the edp-install repository to your local folder.

2. Run the following command:

    ``
    make docs
    ``

3. Enter the localhost:8000 address in the browser and check that documentation pages are available.

4. Open the file editor, navigate to edp-install->docs and make necessary changes. Check all the changes at localhost:8000.

5. Create a merge request with changes.

## MkDocs Without Docker

Prerequisites:

* Git is installed. Please refer to the [Git downloads](https://git-scm.com/downloads).

* [Python 3.9.5](https://www.python.org/downloads/windows/) is installed.

To work with MkDocs without Docker, take the following steps:

1. Clone the edp-install repository to your local folder.

2. Run the following command:

    ```
    pip install -r  hack/mkdocs/requirements.txt
    ```

3. Run the local development command:

    ```
    mkdocs serve --dev-addr 0.0.0.0:8000
    ```
  !!! Note
      This command may not work on Windows, so a quick solution is:
      ```
      python -m mkdocs serve --dev-addr 0.0.0.0:8000
      ```

4. Enter the localhost:8000 address in the browser and check that documentation pages are available.

5. Open the file editor, navigate to edp-install->docs and make necessary changes. Check all the changes at localhost:8000.

6. Create a merge request with changes.
