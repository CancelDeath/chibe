This page demonstrates an example use of ChiBE. For detailed description of ChiBE features please refer to the [User Guide](http://www.cs.bilkent.edu.tr/~ivis/chibe/ChiBE-2.1.UG.pdf).


Open ChiBE and go "Query --> Pathway Commons (Level 3) --> Paths From To ...". Search for paths from AR to TP53 with length limit 3.

![http://wiki.chibe.googlecode.com/hg/images/from-to-dialog.png](http://wiki.chibe.googlecode.com/hg/images/from-to-dialog.png)

You will get the following path.

![http://wiki.chibe.googlecode.com/hg/images/chibe-ar-tp53-result.png](http://wiki.chibe.googlecode.com/hg/images/chibe-ar-tp53-result.png)

Load profiling data of TCGA prostate cancer study from cBioPortal by going "Data --> Fetch from cBio Portal ...".

![http://wiki.chibe.googlecode.com/hg/images/portal-dialog1.png](http://wiki.chibe.googlecode.com/hg/images/portal-dialog1.png)

Node colors are updated with the alteration frequencies.

![http://wiki.chibe.googlecode.com/hg/images/chibe-ar-tp53-cbio-prostate-loaded.png](http://wiki.chibe.googlecode.com/hg/images/chibe-ar-tp53-cbio-prostate-loaded.png)

Alteration frequencies are also displayed in the tooltip text. TP53 is altered in 11% of the loaded cases. The details of the alteration data can be reached through the right-click menu of the nodes.

![http://wiki.chibe.googlecode.com/hg/images/right-click-to-portal-details.png](http://wiki.chibe.googlecode.com/hg/images/right-click-to-portal-details.png)

Below window displays the types of the loaded alterations and alteration ratios for each type.

![http://wiki.chibe.googlecode.com/hg/images/portal-details.png](http://wiki.chibe.googlecode.com/hg/images/portal-details.png)

TP53 is lost in 4 cases and mutated in 5 cases.

Let's check if any protein here is differentially expressed between primary prostate samples and metastatic samples. Such an experiment exists in GEO database with ID GSE3325.

Go to "Data --> Fetch From GEO ...".

![http://wiki.chibe.googlecode.com/hg/images/GEO-dialog.png](http://wiki.chibe.googlecode.com/hg/images/GEO-dialog.png)

By default, ChiBE uses the comparison of the first two experiments in the loaded dataset. To compare primary tumors with metastatic, go to "Data --> Data Selection ...", and select to compare related samples.

![http://wiki.chibe.googlecode.com/hg/images/data-use-dialog.png](http://wiki.chibe.googlecode.com/hg/images/data-use-dialog.png)

First group is the primary tumors and the second is the metastatic ones in the dataset. Node colors are updated accordingly.

![http://wiki.chibe.googlecode.com/hg/images/chibe-exp-loaded.png](http://wiki.chibe.googlecode.com/hg/images/chibe-exp-loaded.png)

So we see that metastatic tumors have higher expression of AR, MAPK11, GNG2, GNAI2, and lower expression for TP53 and GNAI1, in that particular dataset.