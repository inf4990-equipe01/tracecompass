Trace Compass Test Traces
=========================

This tree contains a set of CTF test traces, primarily for use in Trace Compass.

To build the package and install it in your local Maven repo, simply isssue

    mvn clean install

You can also use the `deploy` target to populate both a standard Maven repo and
a p2 update site. The `-Dmaven-deploy-destination` and `-Dp2-deploy-destination`
properties can be used to specify their respective deploy locations.
For example:

    mvn clean deploy -Dmaven-deploy-destination=file:///var/www/traces/maven -Dp2-deploy-destination=/var/www/traces/repository

(Note that the first property needs a `file:///` scheme, but the second does not.) 

You can then point depending projects to these locations.


Adding a new CTF test trace
---------------------------

The modules follow the [Maven standard directory layout][].

To add a new CTF test trace, add it to the `ctf/src/main/resources` directory.
Make sure it is not archived or anything, as this will be exposed as-is to the
users.

Then update the `ctf/src/main/java/.../CtfTestTrace.java` file accordingly to
include the new trace.

Make sure the parameters (event count, etc.) are correct! This project does not
check those at the moment, but if they are incorrect they **will** fail some
Trace Compass unit tests. This is a known issue.

Finally, bump the project's minor version (1.1.0 -> 1.2.0) in the main pom.xml
and related `<parent>` blocks.


Deploying the repo and update site
----------------------------------

The default `mvn deploy` goal, when run from the Eclipse CI servers, will deploy
to the following locations:

* Standard Maven repo (for use in Maven `<dependency>` blocks)
  * <http://archive.eclipse.org/tracecompass/tracecompass-test-traces/maven/>
* p2 update site (for use in Eclipse .target files)
  * <http://archive.eclipse.org/tracecompass/tracecompass-test-traces/repository/latest/>

When pushing a new version, some extra work is required on the server to update
the p2 update site. The `/repository/` is directory a actually a
[p2 composite repository][]. But since the deploy simply overwrites the contents
of `/repository/latest/`, you need to do the following steps manually:

* Copy the `/latest/` directory to a new directory named after the new version,
  like `/1.2.0/` (*copy* not *move*, please keep `/latest/` available too).
* Add a new entry in the `compositeArtifacts.xml` and `compositeContent.xml`
  files to point to the new directory. Do **not** delete existing entries, other
  projects or git branches may still be using those.

No extra steps are required for the Maven repo, since the Maven plugin handles
multi-version deploying automatically.

[Maven standard directory layout]: https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html
[p2 composite repository]: https://wiki.eclipse.org/Equinox/p2/Composite_Repositories_%28new%29

