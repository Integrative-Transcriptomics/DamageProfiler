General Usage
=============


DamageProfiler can be used to calculate and viualize damage patterns in ancient DNA. As input, a mapping file (sam, bam, or cram format) is expected. The result is provided in both graphic and text-based representation. DamageProfiler can be used in offline mode, however, idenitfying the species name when running multi-reference mapping files is not possible. 

It creates

    * damage plots
    * fragment length distribution
    * edit distances (number of bases that differ between read and reference)
    * base frequency table of reference (if reference is specified)
    * base frequency table of input file
    * table of different base misincorporations and their occurrences


How to run
----------

.. code-block:: console

    java -jar DamageProfiler-VERSION.jar [options]



Options:

    * **-h** 
    Shows this help page.

    * **-version** 
    Shows the version of DamageProfiler.

    * **-i <INPUT>** 
    The input sam/bam/cram file.

    * **-r <REFERENCE>** 
    The reference file (fasta format). 
    
    * **-o <OUTPUT>** 
    The output folder. Please specify the path to the result folder here. The folder structure will be as following: 
   
    - The mapping file contains the mapping against one reference genome (parameters -s and -sf are not set):
      The folder specified with '-o' is used as the main result folder. For the actual results, an additional folder within this folder is created, named according the file name. If you run multiple files with the same output folder, the results will not be overwritten but stored separately.
      
      Example:  

      -i mapping_file_sample_A.bam -o /home/neukamm/results_damageprofiler/
      The result files will then be stored in /home/neukamm/results_damageprofiler/mapping_file_sample_A/.

    - The mapping file is a multi-referene/metagenomic mapping file and a list of species was specified via the parameters -s or -sf:
      Same as above, but for each species, a separate folder is created.
      
      Example:  
 
      -i mapping_file_sample_B.bam -o /home/neukamm/results_damageprofiler/ -s 'NC_002677.1'
      The results will be stored in /home/neukamm/results_damageprofiler/mapping_file_sample_B/NC_002677.1_Mycobacterium_leprae_TN/

      So if multiple species are specified, all results can be found under one folder: /home/neukamm/results_damageprofiler/mapping_file_sample_B/

    
    * **-t <THRESHOLD>**
    Number of bases which are considered for plotting nucleotide misincorporations in the damage plot. Default: 25.

    * **-s <SPECIES>**
    Reference sequence name (Reference NAME flag of SAM record). Depending on which database was used for mapping, this is the accession ID of the reference (i.e. NCBI accession ID).

    * **-sf <SPECIES FILE>**
    List with accession IDs of species for which damage profile has to be calculated. This file is a text file, with one species entry per line. 

    * **-l <LENGTH>**
    Number of bases which are considered for frequency computations. Default: 100.

    * **-title <TITLE>**
    Title used for all plots. Default: input filename.

    * **-yaxis_dp_max <MAX_VALUE>**
    Maximal y-axis value that is visualized in the damage plot.

    * **-color_c_t <COLOR_C_T>** 
    Color for the line representing the C to T misincoporation frequency in the damage plot. The colour should be given as hex colour code (i.e. for magenta, set #ff00ff).

    * **-color_g_a <COLOR_G_A>** 
    Color for the line representing the G to A misincoporation frequency in the damage plot. The colour should be given as hex colour code (i.e. for magenta, set #ff00ff).

    * **-color_instertions <COLOR_C_T>**
    Color for the line representing base insertions in the damage plot. The colour should be given as hex colour code (i.e. for magenta, set #ff00ff).

    * **-color_deletions <COLOR_DELETIONS>**
    Color for the line representing base deletions in the dmage plot. The colour should be given as hex colour code (i.e. for magenta, set #ff00ff).

    * **-color_other <COLOR_OTHER>**
    Color for the line representing other bases misincorporations in the damage plot.  The colour should be given as hex colour code (i.e. for magenta, set #ff00ff).

    * **-only_merged**
    Use only mapped and merged (in case of paired-end sequencing) reads to calculate damage plot instead of using all mapped reads. The SAM/BAM entry must start with 'M\_', otherwise it will be skipped. Default: false

    * **-sslib**
    Single-stranded library protocol was used. Default: false. This option only highlights the C to T base misincorporations in the damage plot.





GUI 
-----

Running the jar file without any parameter starts the GUI to configure the run:

.. image:: images/DP_main.png
   :width: 400px
   :height: 400px
   :align: center



Log file
--------

DamageProfiler documents the configuration in a separate log file, which helps you to reproduce your analysis at a later date.
The file is saved in the specified result folder.


