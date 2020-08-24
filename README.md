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
- fragment length distribution
- read identity distribution 
- base frequency table of reference 
- table of different base misincorporations and their occurrences


###### How to run

```
java -jar DamageProfiler-VERSION.jar -i input_file -o output_folder [options]
```

Running the jar file without any parameter starts the GUI to configure the run.


**-h**\
Shows this help page.\
\
**-version**\
Shows the version of DamageProfiler.\
\
**-i INPUT**\
The input sam/bam/cram file (Required).\
\
**-o OUTPUT**\
The output folder (Required).\
\
**-r REFERENCE**\
The reference file (fasta format).\
\
**-t THRESHOLD**\
DamagePlot: Number of bases which are considered for plotting nucleotide misincorporations. Default: 25.\
\
**-s SPECIES**\
Reference sequence name (Reference sequence name (SN tag) of the SAM record). Species must be put in 
quotation marks (e.g. -s 'NC_032001.1|tax|1917232|'), multiple species must be comma separated 
(e.g. -s 'NC_032001.1|tax|1917232|,NC_031076.1|tax|1838137|,NC_034267.1|tax|1849328|').Please specify either -s or -sf.\
\
**-sf FILE SPECIES**\
Text file containing a list with species (Reference sequence name (SN tag) of the SAM record, one per line) for which damage profile has 
to be calculated. Please specify either -s or -sf.\
\
**-l LENGTH**\
Number of bases which are considered for frequency computations. Default: 100.\
\
**-title TITLE**\
Title used for all plots. Default: input filename.\
\
**-yaxis_dp_max MAX_VALUE**\
DamagePlot: Maximal y-axis value.\
\
**-color_c_t COLOR_C_T**\
DamagePlot: Color (HEX code) for C to T misincoporation frequency.\
\
**-color_g_a COLOR_G_A**\
DamagePlot: Color (HEX code) for G to A misincoporation frequency.\
\
**-color_instertions COLOR_C_T**\
DamagePlot: Color (HEX code) for base insertions.\
\
**-color_deletions COLOR_DELETIONS**\
DamagePlot: Color (HEX code) for base deletions.\
\
**-color_other COLOR_OTHER**\
DamagePlot: Color (HEX code) for other bases different to reference.\
\
**-only_merged**\
Use only mapped and merged (in case of paired-end sequencing) reads to calculate damage plot instead of using all mapped reads. The SAM/BAM entry must start with 'M_', otherwise it will be skipped. Default: false.\
\
**-sslib**\
Single-stranded library protocol was used. Default: false.

-------------------------------------------------------

A more detailed documentation of DamageProfiler is available at 
https://damageprofiler.readthedocs.io.