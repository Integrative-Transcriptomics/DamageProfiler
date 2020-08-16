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
Reference sequence name (Reference NAME flag of SAM record). For more details see Documentation.\
\
**-sf SPECIES LIST**\
List with species for which damage profile has to be calculated. For more details see Documentation.\
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
DamagePlot: Color for C to T misincoporation frequency.\
\
**-color_g_a COLOR_G_A**\
DamagePlot: Color for G to A misincoporation frequency.\
\
**-color_instertions COLOR_C_T**\
DamagePlot: Color for base insertions.\
\
**-color_deletions COLOR_DELETIONS**\
DamagePlot: Color for base deletions.\
\
**-color_other COLOR_OTHER**\
DamagePlot: Color for other bases different to reference.\
\
**-only_merged**\
Use only mapped and merged (in case of paired-end sequencing) reads to calculate damage plot instead of using all mapped reads. The SAM/BAM entry must start with 'M_', otherwise it will be skipped. Default: false.\
\
**-sslib**\
Single-stranded library protocol was used. Default: false.

-------------------------------------------------------

Stay tuned, a more detailed description, manual and tutorial of DamageProfiler is coming soon.
