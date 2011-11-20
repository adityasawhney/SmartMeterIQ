#!/usr/bin/perl -w

$exit_value = 0;

@build_dirs = ("../bignat/_java_build_dir", 
	       "../test/_java_build_dir",
	       "../front/_java_build_dir",
	       "../gui/_java_build_dir");

# @build_dirs = (
# 	       "../test/_java_build_dir",
# 	       "../front/_java_build_dir",
# 	       "../gui/_java_build_dir");

$doc_dir = "../_doc_build_dir";
@doc_packages = ("ds/ov2/util",
		 "ds/ov2/bignat",
		 "ds/ov2/test",
		 "ds/ov2/front",
		 "ds/ov2/gui");

sub checkdoc($$) {
    my ($pack, $file) = @_;

    # print "Check $pack / $file\n";

    if(-f "$doc_dir/$pack/$file") {
	return;
    }

    foreach my $p (@doc_packages) {
	if(-f "$doc_dir/$p/$file") {
	    return;
	}
    }

    print "MISSING file $pack/$file\n";

    $exit_value = 1;
}


sub readbuilddir($$$);

sub readbuilddir($$$) {
    my ($build, $project, $pack) = @_;
    my $dir = $project eq "" ? $build : "$build/$project";
    $dir = $pack eq "" ? $dir : "$dir/$pack";
    #print "scanning $dir\n";

    opendir(DIR, $dir) || die "can't opendir $dir: $!";
    my @entries = readdir(DIR);
    closedir DIR;

    foreach my $f (@entries) {
	next if($f eq ".");
	next if($f eq "..");
	next if(-l "$dir/$f");
	
	if(-d "$dir/$f") {
	    my $subproj = $project eq "" ? $f : $project;
	    my $subpack = "";
	    if($project ne "") {
		$subpack = $pack eq "" ? $f : "$pack/$f";
	    }

	    readbuilddir($build, $subproj, $subpack);
	}
	elsif($f =~ /.java/) {
	    checkdoc($pack, $f);
	}
    }
    #print "finished with $dir\n";
}


foreach my $d (@build_dirs) {
    readbuilddir($d, "", "");
}

exit $exit_value;
