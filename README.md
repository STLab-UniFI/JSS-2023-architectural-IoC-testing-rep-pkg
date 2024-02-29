# Unveiling Faulty User Sequences: A Model-based Approach to Test Three-Tier Software Architectures

This repository is a companion page for the following publication, accepted to the [Journal of Systems and Software]([https://onlinelibrary.wiley.com/journal/10991689](https://www.sciencedirect.com/journal/journal-of-systems-and-software)):
> Leonardo Scommegna, Roberto Verdecchia and Enrico Vicario. 2023. Unveiling Faulty User Sequences: A Model-based Approach to Test Three-Tier Software Architectures.

It contains the proof of concept implementation of the approach, the documentation of the _Flight Manager_ web application, and all the material to replicate the results of the experimental proof of concept, including:
installation steps, test suites, and all 32 faulty versions of _Flight Manager_.

## How to cite us
 
If this study is helping your research, consider citing it as follows, thanks!

```
@article{scommegna2024unveiling,
  title={Unveiling Faulty User Sequences: A Model-based Approach to Test Three-Tier Software Architectures},
  author={Scommegna, Leonardo and Verdecchia, Roberto and Vicario, Enrico},
  journal={Journal of Systems and Software},
  year={2024},
  publisher={Elsevier}
}
```

## Proof of Concept Implementation

The documentation and the source code for the experimental proof of concept implementation are available [here](PoC-implementation).

## Flight Manager Documentation

The documentation about the experimental object, _Flight Manager_ is available [here](documentation/docs/flight-manager.md).



## Experimental Proof of Concept Results and Data


This repository contains both the original _Flight Manager_ version and the 32 mutant versions.
All the 32+1 versions of _Flight Manager_ are available [here](flightmanager-versions).

Each version contains all the test suites used in the reference paper.
Test suites are available at the path `flightmanager-versions/version-id/src/e2e/java` where `version-id` is the name of a subdirectory, either `faulty-version-*` or `original-version`.
In particular:
1. `allEdgesTest` contains the test suite obtained with the _All Edges_ coverage criterion on the mcDFG
2. `allNodesTest` contains the test suite obtained with the _All Nodes_ coverage criterion on the mcDFG
3. `allDUPaths` contains the test suite obtained with the _All DU Paths_ coverage criterion on the mcDFG
4. `allUsesTest` contains the test suite obtained with the _All Uses_ coverage criterion on the mcDFG
5. `allDefsTest` contains the test suite obtained with the _All Defs_ coverage criterion on the mcDFG
6. `pndCoverages` contains the test suites obtained with the _All Pages_ and _All Navigations_ coverage criteria on the PND

Further documentation about the test suites and the _Flight Manager_ faulty versions are available [here](documentation/docs/experimental-proof-of-concept.md)


## Quick start

In order to replicate the experimental proof of concept follow these steps:

### Getting started

1. Clone the repository 
   - `git clone git@github.com:STLab-UniFI/JSS-2023-architectural-IoC-testing-rep-pkg.git`
2. Choose a _Flight Manager_ version from those available in the `flightmanager-versions` folder and open it with an IDE of your choice
3. Update the project with Maven
4. Run the class `/src/e2e/java/it/unifi/dinfo/stlab/flightmanager/util/H2Manager.java` with the command line argument `start` to setup the H2 in-memory DB automatically
5. Exec the test suites at the path  `/src/e2e/java`

### Troubleshooting

Although once built through Maven everything should configure automatically, sometimes Selenium WebDriver might encounter issues related to browser drivers. It is recommended to resolve them by following the guide at this [link](https://www.selenium.dev/documentation/webdriver/drivers/)

## Repository Structure
This is the root directory of the repository. The directory is structured as follows:

    JSS-2023-architectural-IoC-testing-rep-pkg
     .
     |
     |--- documentation/                    Further structured documentation of the replication package content
     |      |
     |      |--- docs/                      Markdown documents
     |      |   |
     |      |   |--- faulty-versions        Documents about the Flight Manager Faulty Versions
     |      |   |
     |      |   |--- test-suites            Documents about the test-suites
     |      |
     |      |--- imgs/                      Images used for the documentation
     |
     |--- PoC-implementation/               Proof of Concept implementation Code
     |
     |--- flightmanager-versions/           Flight Manager versions
            |
            |--- original-version/          Flight Manager version with no injected faults      
            |
            |--- faulty-version-01/         Flight Manager version with fault #1 injected      
            |
            |--- ...                        
            |
            |--- faulty-version-32/         Flight Manager version with fault #32 injected
