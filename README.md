# DamageProfiler
[![Build Status](https://travis-ci.org/Integrative-Transcriptomics/DamageProfiler.svg?branch=master)](https://travis-ci.org/Integrative-Transcriptomics/DamageProfiler)
[![Documentation Status](https://readthedocs.org/projects/damageprofiler/badge/?version=latest)](http://damageprofiler.readthedocs.io/en/latest/?badge=latest)
[![DOI](https://zenodo.org/badge/84447018.svg)](https://zenodo.org/badge/latestdoi/84447018)
[![install with bioconda](https://img.shields.io/badge/install%20with-bioconda-brightgreen.svg?style=flat-square)](http://bioconda.github.io/recipes/damageprofiler/README.html)

This method can be used to calculate damage profiles of mapped ancient DNA reads. 

Main author: Judith Neukamm <judith.neukamm@uzh.ch>
Contributor: Alexander Peltzer <alexander.peltzer@uni-tuebingen.de>

###### Method
DamageProfiler calculates damage profiles of mapped reads and provides a 
graphical as well as texted based representation. 

It creates 
- damage plots
- a fragment length distribution
- a read identity distribution 
- a base frequency table of reference 
- a table of different base misincorporations and their occurencies


###### How to run

```
java -jar DamageProfiler-VERSION.jar
```

Running the jar file without any parameter starts a GUI to configure the run. 


Parameters:

**-h,--help**\
Shows this help page\
\
**-i,--input <INPUT>**\
The input sam/bam file\
\
**-l,--length <LENGTH>**\
Number of bases which are computations\
\
**-mapped,--all_mapped_reads**\
Use all mapped reads to calculate damage plots. Default: false\
\
**-o,--output <OUTPUT>**\
The output folder\
\
**-r,--reference <REFERENCE>**\
The reference file\
\
**-s,--specie <SPECIE>**\
Reference identifier. This is made for SAM/BAM files containing multiple references and allows to run DamageProfiler on single references
without pre-filtering the sam file.\
Example: NC_022116.1
\
If you use MALT, please make sure that you run MALT without *--sparseSAM* option. This will create a sam file with is not readable.
\
\
**-sf,--specieslist file <SPECIES LIST>**\
List with species for which damage profile has to be calculated. Given as txt file, one species (reference identifier) per line. 
Species has to have the same format as *-s* parameter.\
\
**-t,--threshold <THRESHOLD>**\
Number of bases which are considered for plotting nucleotide misincorporations.\
\
**-title,--title <TITLE>**\
Title used for all plots. Default: input file name\
\
**-yaxis,--yaxis <YAXIS>**\
Max value on y axis shown in all plots.\



Stay tuned, a more detailed description, manual and tutorial of DamageProfiler is coming soon.



