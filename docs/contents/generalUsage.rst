General Usage
=============


DamageProfiler can be used to calculate damage profiles of mapped ancient DNA reads given as SAM/BAM file and provides
a graphical as well as text based representation.

It creates

    * damage plots
    * a fragment length distribution
    * a read identity distribution
    * a base frequency table of reference
    * a table of different base misincorporations and their occurrences


How to run
----------

.. code-block:: bash
java -jar DamageProfiler-VERSION.jar <arguments> [options]


Arguments:

    * **-h,--help** : Shows this help page.
    * **-i,--input <INPUT>** : The input sam/bam file.
    * **-o,--output <OUTPUT>** : The output folder

Options:

    * **-l,--length <LENGTH>** : Number of bases which are computations.
    * **-mapped,--all_mapped_reads** : Use all mapped reads to calculate damage plots. Default: false.
    * **-r,--reference <REFERENCE>** : The reference file. The reference is only needed if the mapping file doesn't have MD tags.
    * **-s,--specie <SPECIE>** : RefSeq ID of the reference genome. This will just use the reads mapping to the specific reference for creating the damage profile. It can for example be used for MALT output files. In this case, please make sure that you run MALT without *--sparseSAM* option. This would create a sam file with is not readable. Example: -s NC_022116.1
    * **-sf,--specieslist file <SPECIES LIST>** : List with species (RefSeq IDs) for which damage profile has to be calculated. One species per line, given as text file (.txt). Species must have the same format like *-s* parameter.
    * **-t,--threshold <THRESHOLD>** : Number of bases which are considered for plotting nucleotide misincorporations.
    * **-title,--title <TITLE>** : Title used for all plots (Default: file name of input SAM/BAM file).
    * **-yaxis,--yaxis <YAXIS>** : Maximal value on y axis (Default: flexible, adapts to the calculated damage).


DamageProfiler can be used in offline mode.
Running the jar file without any parameter starts a org.damageprofiler.GUI to configure the run.


Log file
--------

DamageProfiler documents the configuration in a separate log file, which helps you to reconstruct your analysis at a later date.
The file is saved in the specified result folder.
