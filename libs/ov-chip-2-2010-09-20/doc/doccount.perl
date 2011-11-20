#!/usr/bin/perl -w

$doc_dir = "generated/ds/ov2/*/*.html";
$src_dir = "../";

@files = glob($doc_dir);

# fill the inner hash with inner classes
foreach $f (@files) {
    # jump over package summary and use files
    next if $f =~ /-/;
    @xxx = split( "/", $f);
    $base = pop(@xxx);
    # For inner classes the filename contains a dot, like A.Inner.html
    @split_base = split(/\./, $base);
    if( @split_base > 2) {
	push(@{$inner{$split_base[0]}}, $f);
    }
}

$total_needed = 0;
$total_present = 0;

# start counting now
foreach $f (@files) {
    # jump over package summary and use files
    next if $f =~ /-/;
    @xxx = split( "/", $f);
    $base = pop(@xxx);		# the base name
    $pack = pop(@xxx);		# the package name
    # Inner classes contain at least two dots, like A.Inner.html
    @split_base = split(/\./, $base);
    # Jump over the inner classes, they are done when their enclosing
    # class is met.
    if( @split_base == 2) {
	$doc_needed = 0;
	$doc_present = 0;
	$class_name = $split_base[0];
	open(FILE, $f);
	# The summaries have one line for each field in TableRowColor.
	# But also the line methods inherited from is in this color.
	while(<FILE>) {
	    $doc_needed++ if /TableRowColor/;
	    $doc_needed-- if /Methods inherited from/;
	    $doc_needed-- if /Fields inherited from/;
	    $doc_needed-- if /Nested classes\/interfaces inherited from class/;
	    # enums have two default methods, for which
	    # javadoc automatically generates documentation
	    $doc_needed -= 2 if /Enum Constant Summary/;
	}
	close FILE;
	# For outer classes add one for the class description.
	$doc_needed++;
	if($inner{$class_name}) {
	    foreach $i (@{$inner{$class_name}}) {
		#print STDERR "inner $i for $class_name\n";
		open(FILE, $i);
		# The summaries have one line for each field in TableRowColor.
		# But also the line methods inherited from is in this color.
		while(<FILE>) {
		    $doc_needed++ if /TableRowColor/;
		    $doc_needed-- if /Methods inherited from/;
		    $doc_needed-- if /Fields inherited from/;
		    # enums have two default methods, for which
		    # javadoc automatically generates documentation
		    $doc_needed -= 2 if /Enum Constant Summary/;
		}
		close FILE;
		# Inner classes also need a class description. 
		# But this requirement is already counted in the 
		# enclosing class in the inner class summary.
	    }
	}
	$src_file = $src_dir . $pack . "/" . $class_name . ".java";
	die "cannot open $src_file for $f" unless open(FILE, $src_file);
	while(<FILE>) {
	    $doc_present++ if m|/\*\*|;
	}
	close FILE;

	$package_sum{$pack}->{"needed"} += $doc_needed;
	$package_sum{$pack}->{"present"} += $doc_present;

	$total_needed += $doc_needed;
	$total_present += $doc_present;

	$doc{$f}->{"class"} = $class_name;
	$doc{$f}->{"pack"} = $pack;
	$doc{$f}->{"needed"} = $doc_needed;
	$doc{$f}->{"present"} = $doc_present;
	$doc{$f}->{"prozent"} = $doc_present * 100 / $doc_needed; 
    }
}


printf("doc level %.02f%% %d/%d\n\n", 
       $total_present * 100 / $total_needed,
       $total_present,
       $total_needed);


$file_number = 0;
$files_finished = 0;
foreach $k (keys %doc) {
    $file_number++;
    $files_finished++ if($doc{$k}->{"needed"} == $doc{$k}->{"present"});
}

printf("%d/%d files completed (%.1f%%)\n\n",
       $files_finished,
       $file_number,
       $files_finished * 100 / $file_number);


%package_sort=("util" => 1,
	       "bignat" => 2,
	       "test" => 3,
	       "front" => 4,
	       "gui" => 5
	       );

sub packsort {
    return $package_sort{$a} <=> $package_sort{$b};
}

print "Packages summary\n";
foreach $k (sort packsort (keys %package_sum)) {
    printf("    %s%s\t%5.1f%% (%3d/%3d)\n", 
	   $k, 
	   length($k) < 4 ? "\t" : "",
	   $package_sum{$k}->{"present"} * 100 / 
	   $package_sum{$k}->{"needed"},
	   $package_sum{$k}->{"present"},
	   $package_sum{$k}->{"needed"});
}

print "\n";


sub docsort {
    my %doca = %{$doc{$a}};
    my %docb = %{$doc{$b}};
    my $pack_cmp = 
	$package_sort{$doca{"pack"}} <=> $package_sort{$docb{"pack"}};
    return $pack_cmp if $pack_cmp;
    my $perc_cmp = $doca{"prozent"} <=> $docb{"prozent"};
    return -$perc_cmp if $perc_cmp;
    return $a cmp $b;
}

foreach $k (sort docsort (keys %doc)) {
    printf("%5.1f%% (%2d/%2d) %s.%s\n",
	   $doc{$k}->{"prozent"},
	   $doc{$k}->{"present"}, 
	   $doc{$k}->{"needed"}, 
	   $doc{$k}->{"pack"}, 
	   $doc{$k}->{"class"});
}


# foreach $k (keys %inner) {
#     print $k, " ==> ", join(", ", @{$inner{$k}}), "\n";
# }


