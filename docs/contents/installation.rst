Installation Instructions for DamageProfiler
================================================

JAR file
---------
The tool can be downloaded from `DamageProfiler's GitHub page <https://github.com/Integrative-Transcriptomics/DamageProfiler/releases>`_.
After downloading the JAR file, you can start the application via double click on most operating systems (OSX, Windows, and Linux).
If not, please either install Java 11 or higher on your workstation:

.. code-block:: console

    sudo apt install default-jdk

or make the file executable:


.. code-block:: console

    sudo chmod u+x DamageProfiler-1.0.jar


Bioconda
---------
For easy installation, DamageProfiler is also available as a `bioconda package <https://anaconda.org/bioconda/damageprofiler>`_ and can be installed with one of the following

On Ubuntu:

.. code-block:: console
   
   conda install -c bioconda damageprofiler
   conda install -c bioconda/label/cf201901 damageprofiler

At the moment, only DamageProfiler version 0.4.9 is available via bioconda.


Old releases
-------------

Old releases can be found at `GitHub <https://github.com/Integrative-Transcriptomics/DamageProfiler/releases>`_.
