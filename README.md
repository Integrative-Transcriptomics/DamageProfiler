# DamageProfiler
[![Build Status](https://travis-ci.org/Integrative-Transcriptomics/DamageProfiler.svg?branch=master)](https://travis-ci.org/Integrative-Transcriptomics/DamageProfiler)
[![Documentation Status](https://readthedocs.org/projects/damageprofiler/badge/?version=latest)](http://damageprofiler.readthedocs.io/en/latest/?badge=latest)
[![DOI](https://zenodo.org/badge/84447018.svg)](https://zenodo.org/badge/latestdoi/84447018)
[![install with bioconda](https://img.shields.io/badge/install%20with-bioconda-brightgreen.svg?style=flat-square)](http://bioconda.github.io/recipes/damageprofiler/README.html)

This method can be used to calculate damage profiles of mapped ancient DNA reads. 

Main author: Judith Neukamm <judith.neukamm@uzh.ch>\
Contributor: Alexander Peltzer <alexander.peltzer@qbic.uni-tuebingen.de>

###### Method
DamageProfiler calculates damage profiles of mapped reads and provides a 
graphical as well as text based representation. 

It creates 
- damage plots
- a fragment length distribution
- a read identity distribution 
- a base frequency table of reference 
- a table of different base misincorporations and their occurrences


###### How to run

```
java -jar DamageProfiler-VERSION.jar -i input_file -o output_folder [options]
```

Running the jar file without any parameter starts the GUI to configure the run.

<ins>Required Arguments:</ins>

**-h**\
Shows this help page.\
\
**-i INPUT**\
The input sam/bam file.\
\
**-o OUTPUT**\
The output folder.

<ins>Options:</ins>

**-l LENGTH**\
Number of bases which are computations.\
\
**-only_merged**\
Use only all mapped and merged reads to calculate damage plot instead of using all mapped reads. The SAM/BAM entry must start with 'M_', otherwise it will be skipped. Default: false\
\
**-r REFERENCE**\
The reference file\
\
**-s SPECIES**\
Reference sequence name (Reference NAME flag of SAM record). Depending on which database was used for mapping, this is the accession ID of the reference (i.e. NCBI accession ID).\
Example: -s NC_022116.1\
\
**-sf SPECIES LIST**\
List with species (RefSeq IDs) for which damage profile has to be calculated. One species per line, given as text file (.txt). Species must have the same format like *-s* parameter.\
\
**-t THRESHOLD**\
Number of bases which are considered for plotting nucleotide misincorporations.\
\
**-title TITLE**\
Title used for all plots (Default: file name of input SAM/BAM file).\
\
**-yaxis_dp_max MAX_VALUE**\
Maximal value on y axis of damage plot (Default: flexible, adapts to the calculated damage).\
\
**-color_c_t COLOR_C_T**\
DamagePlot: Color for C to T misincoporation frequency.\
\
**-color_g_a COLOR_G_A**\
DamagePlot: Color for G to A misincoporation frequency.\
\
**-color_instertions COLOR_INSERTIONS**\
DamagePlot: Color for base insertions.\
\
**-color_deletions <COLOR_DELETIONS**\
DamagePlot: Color for base deletions.\
\
**-color_other COLOR_OTHER**\
DamagePlot: Color for other bases different to reference.\
\
**-sslib**\
Single-stranded library protocol was used. Default: false.

-------------------------------------------------------

Stay tuned, a more detailed description, manual and tutorial of DamageProfiler is coming soon.
