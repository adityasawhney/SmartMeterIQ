#!/usr/bin/perl -w

#############################################################################
##
##  interface type translation
##
#############################################################################

# A type translation for type t needs the following pieces:
#
#   t                type as it appears in the .id file
#   apdu_type        type that is passed into run_step on the host side
#   interface_type   type that appears in the interface of the _call method
#   value_selector   partial expression that turns a apdu_type into an 
#                    interface_type

$type_translation{"Bignat"} = {
    apdu_type => "APDU_BigInteger",
    interface_type => "BigInteger",
    value_selector => ".value"
    };

$type_translation{"Vector"} = {
    apdu_type => "Host_vector",
    interface_type => "Host_vector",
    value_selector => ""
    };

$type_translation{"Modulus"} = {
    apdu_type => "Host_modulus",
    interface_type => "Host_modulus",
    value_selector => ""
    };

$type_translation{"APDU_short"} = {
    apdu_type => "APDU_short",
    interface_type => "int",
    value_selector => ".value"
    };

$type_translation{"APDU_short_array"} = {
    apdu_type => "APDU_short_array",
    interface_type => "int[]",
    value_selector => ".get_int_array()"
    };

$type_translation{"APDU_byte"} = {
    apdu_type => "APDU_byte",
    interface_type => "byte",
    value_selector => ".value"
    };

$type_translation{"APDU_boolean"} = {
    apdu_type => "APDU_boolean",
    interface_type => "boolean",
    value_selector => ".value"
    };

$type_translation{"Signature"} = {
    apdu_type => "Host_signature",
    interface_type => "Host_signature",
    value_selector => ""
    };

# $type_translation("") = {
#     apdu_type => "",
#     interface_type => "",
#     value_selector => ""
#     };


#############################################################################
##
##  subroutines
##
#############################################################################

sub indent($) {
    my ($level) = @_;

    my $i = substr("                                         ", 0, $level);
    return $i;
}

sub print_text_array(*$@) {
    my ($handle, $level, @lines) = @_;

    foreach my $line (@lines) {
        print $handle indent($level), $line, "\n";
    }
}


$hash_line = 
    "//#################################################" . 
    "##########################";


sub print_comment(*$$) {
    my ($handle, $level, $msg) = @_;
    my $i = indent($level);

    print $handle $i, substr($hash_line, 0, 79 - $level), "\n";
    print $handle $i, "// ", $msg, "\n";
    print $handle $i, "// ", "\n\n";
}

sub print_big_comment(*$$) {
    my ($handle, $level, $msg) = @_;
    my $i = indent($level);

    print $handle $i, substr($hash_line, 0, 79 - $level), "\n";
    print $handle $i, substr($hash_line, 0, 79 - $level), "\n";
    print $handle $i, "// ", "\n";
    print $handle $i, "// ", $msg, "\n";
    print $handle $i, "// ", "\n";
    print $handle $i, substr($hash_line, 0, 79 - $level), "\n";
    print $handle $i, substr($hash_line, 0, 79 - $level), "\n\n";
}


sub print_header(*) {
    my ($handle) = @_;
    print $handle <<EOF;
// DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! 
//
// This file has been generated automatically from $file
// by some sort of idl compiler.

EOF
}


sub print_doc(*$$) {
    my ($handle, $level, $description) = @_;

    print $handle indent($level), "/**\n";
    foreach my $line (split('\n', $description)) {
        print $handle indent($level), " * ", $line, "\n";
    }
    print $handle indent($level), " */\n";
}


sub print_class_doc(*$$@) {
    my ($handle, $description, $env, @cpp) = @_;
    my $cpp_test;
    if(@cpp) {
        $cpp_test = "\@CPP This class uses the following cpp defines:\n"
            . join(",\n",
                   map("  <a href=\"../../../overview-summary.html#" .
                       $_ . "\">" . $_ . "</a>",
                       @cpp))
            . "\n";
    }
    else {
        $cpp_test = "\@CPP no cpp preprocessing needed\n";
    }
    print_doc($handle, 0, 
              "$description\n\n" .
              "\@author idl compiler\n" .
              "\@version automatically generated from $file\n" .
              "\@environment $env\n" .
              $cpp_test);
}


sub print_field_doc(*$) {
    my ($handle, $description) = @_;
    print_doc($handle, 4, $description);
}


#############################################################################
##
##  MAIN
##
#############################################################################


print "OV chip 2 protocol idl compiler\n";

die "usage idl.perl input\n" if scalar(@ARGV) ne 1;

$file = $ARGV[0];

$slash_index = rindex($file, "/");
if($slash_index ge 0) {
    $dir = substr($file, 0, $slash_index);
    $class_name = substr($file, $slash_index + 1);
} else {
    $dir = undef;
    $class_name = $file;
}
$point_index = index($class_name, ".");
$class_name = substr($class_name, 0, $point_index);

# $lc_class_name = lc($class_name);

print "Reading $file\n";

open(INPUT, $file) || die "Cannot open input $ARGV[0]\n";


#############################################################################
##
##  Parse input
##
#############################################################################


sub complete_step() {
    if(!defined($current_message)) {
        die("message undefined in protocol " .
            "$current_protocol step $current_step");
    }
    push(@$all_steps, {"name" => $current_step,
                       "call" => $current_call,
                       "args" => [@current_args],
                       "res" => [@current_res],
                       "message" => $current_message,
                       "time" => $current_time
                       });
    $current_step = undef;
    $current_call = undef;
    $current_args = undef;
    $current_res = undef;
    $current_message = undef;
    $current_time = undef;
}

sub complete_protocol() {
    complete_step if $current_step;
    push(@protocols, {"name" => $current_protocol, 
                      "steps" => $all_steps,
                      "delayed_init" => $current_protocol_delayed_init
                      });
    $current_protocol = undef;
    $current_protocol_delayed_init = undef;
    $all_steps = [];
}


while(<INPUT>) {
    next if /^\#/;
    next if m{ *//};
    next if /^[ \t]*$/;
    chomp;
    
    my @lines;
    push(@lines, $_);
    # expand continuation lines
    while(m{\\$}) {
        # chop backslash in $_
        chop;
        # chop same in last element of @lines
        chop $lines[@lines -1];

        my $cont = <INPUT>;
        chomp $cont;
        push(@lines, $cont);
        $_ = $_ . $cont;
        #print "CC $_\n";
    }

    # print "X |$_|\n";

    # package declaration
    if(/^[      ]*package (.*)/) {
        $package = $1;
        next;
    }

    # card imports
    if(/^[      ]*card import (.*)/) {
        push(@card_imports, "import $1;");
        next;
    }

    # host imports
    if(/^[      ]*host import (.*)/) {
        push(@host_imports, "import $1;");
        next;
    }

    # javadoc imports
    if(/^[      ]*javadoc import (.*)/) {
        push(@javadoc_imports, "import $1;");
        next;
    }

    # constructor arguments: additional arguments for 
    # the constructuctor in the _description class
    if(/^[      ]*constructor argument +(\w*) +(.*)/) {
        $type = $1;
        @vars = split /[ ,]+/, $2;
        foreach my $var (@vars) {
            push(@constructor_arguments, [$type, $var]);
        }
        next;
    }

    # constructor statements: additional statements in the 
    # constructor of the description class
    if(/^[      ]*constructor statement +(.*)/) {
        $lines[0] =~ s/^[       ]*constructor statement //;
        $lines[@lines -1] .= ";" unless $lines[@lines -1] =~ /;$/;
        push(@constructor_statements, join("\n", @lines));
        next;
    }

    # Variable declarations
    if(/^[      ]*(card)?var +([][A-Za-z0-9_]+) +(\w*)(.*)/) {
        $type = $2;
        $var = $3;
        $constructor_args = $4;
        #print "XXX var |$1| type |$type| var |$var| con |$constructor_args|\n";
        if($1) {
            # it's a cardvar
            push(@card_variable_declarations, 
                 "/* package local */ $type $var;");
            push(@card_variable_inits, "$var = new $type$constructor_args;")
                unless $constructor_args eq "";
        }
        else {
            # it's a var
            push(@variable_declarations, {var => $var, type => $type });
            push(@variable_inits, "$var = new $type$constructor_args;")
                unless $constructor_args eq "";
        }
        $variable_type{$var}=$type;
        next;
    }

    # Variable type declarations
    if(/^[      ]*vartype +([][A-Za-z0-9_]+) +([\w.]*)/) {
        $type = $1;
        $var = $2;
        #print "XXX vartype type |$type| var |$var|\n";
        $variable_type{$var}=$type;
        next;
    }

    # host initializers
    if(/^[      ]*host initializer +([\w.]*) (.*)/) {
        push(@{$host_stub_initializer{$1}}, $2);
        next;
    }

    # includes
    if(/^[      ]*include (.*)/) {
        push(@includes, "#include \"$1\"");
        next;
    }

    # new protocol
    if(/^[      ]*protocol (\w*)/) {
        complete_protocol() if $current_protocol;
        $current_protocol = $1;
        $all_steps = [];
        next;
    }

    # delayed protocol initialization
    if(/^[      ]*delayed protocol init/) {
        $current_protocol_delayed_init = 1;
        next;
    }

    # new step
    if(/^[      ]*step (\w*) *:(.*)->(.*)/) {
        complete_step() if $current_step;
        $current_step = $1;
        @current_args = split /[ \t,]+/, $2;
        # split returns a leading whitespace field...
        if(@current_args > 0) {
            shift @current_args if $current_args[0] eq "";
        }
        @current_res = split /[ \t,]+/, $3;
        # split returns a leading whitespace field...
        if(@current_res > 0) {
            shift @current_res if $current_res[0] eq "";
        }
        #print "Step $current_step\n";

        foreach my $v (@current_args, @current_res) {
            die "$file:$.:variable $v undeclared\n" unless $variable_type{$v};
        }

        next;
    }

    if(/^[      ]*message (.*)/) {
        $current_message = $1;
        next;
    }

    if(/^[      ]*measure time/) {
        $current_time = 1;
        next;
    }

    if(/^[      ]*call nothing/) {
        $current_call = undef;
        next;
    }

    if(/^[      ]*call /) {
        $lines[0] =~ s/^[       ]*call //;
        $current_call = join("\n", @lines);
        next;
    }

    die "$file:$.: Unrecognized line $_\n";
    
}

complete_protocol() if $current_protocol;

close INPUT;

die "$file:$.: package declaration missing\n" unless $package;


#############################################################################
#############################################################################
##
##  Generate description
##
#############################################################################
#############################################################################


if($dir) {
    $descr_file = $dir . "/" . $class_name . "_description.java";
}
else {
    $descr_file = $class_name . "_description.java";
}    
print("Generating $descr_file\n");
$mask=umask;
umask($mask | 0222);
unlink $descr_file;
open(OUT, ">", $descr_file) || die "Cannot open $descr_file";
umask($mask);

print_header(OUT);

print OUT <<EOF;
#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package $package;
#endif


#if defined(JAVACARD_APPLET) || defined(JAVADOC)
  import javacard.framework.APDU;
EOF

print_text_array(OUT, 2, @card_imports);

print OUT <<EOF;
#endif
#if !defined(JAVACARD_APPLET) || defined(JAVADOC)
  import ds.ov2.util.APDU_Serializable;
  import ds.ov2.util.Protocol_step;
  import ds.ov2.util.Protocol;
EOF

print_text_array(OUT, 2, @host_imports);

print OUT <<EOF;
#endif

#if defined(APPLET_TESTFRAME) || defined(JAVADOC)
   import ds.ov2.util.Void_method;
   import ds.ov2.util.Empty_void_method;
#endif

EOF

if(@javadoc_imports) {
    print OUT "#ifdef JAVADOC\n";
    print_text_array(OUT, 2, @javadoc_imports);
    print OUT "#endif\n\n\n";
}

print_class_doc(OUT, 
                "Protocol description for $class_name. " .
                "Defines suitable Protocol's and Protocol_steps " .
                "for all protocols described in $file " .
                "for use in the OV-chip protocol layer.",
                "host, card",
                ("PACKAGE", "JAVACARD_APPLET", "APPLET_TESTFRAME", 
                 "PUBLIC", "ASSERT"));

print OUT <<EOF;
PUBLIC class ${class_name}_description {

    //#########################################################################
    // Variable declarations
    // 

EOF

if(@card_variable_declarations) {
    print OUT "    #if defined(JAVACARD_APPLET) || ",
        "defined(APPLET_TESTFRAME) || defined(JAVADOC)\n";

    foreach my $card_var (@card_variable_declarations) {
        print_doc(OUT, 8,
                  "Card variable declararion from $file.\n" .
                  "<P>\n" .
                  "Only available if either JAVACARD_APPLET\n" .
                  "or APPLET_TESTFRAME is defined.");
        print OUT indent(8), $card_var, "\n\n";
    }

    print OUT "    #endif\n\n";
}


#print_text_array(OUT, 4, @variable_declarations);
foreach my $v (@variable_declarations) {
    print_field_doc(OUT, "Variable declaration from $file.");
    print OUT indent(4);
    print OUT "/* package local */ ";
    print OUT $v->{type}, " ", $v->{var}, ";\n";
}

print OUT "\n\n";

print_text_array(OUT, 4, @includes);
print OUT "\n\n" if(@includes > 0);



##############################################################################
# 
# Protocol definitions
# 
##############################################################################


foreach my $p (@protocols) {
    my $name = $$p{"name"};
    my $lc_name = lc($name);
    my $steps=$$p{"steps"};
    my $delayed_init = $$p{"delayed_init"};
    my @step_init_statements;
    my @step_update_statements;
    my @step_list;
    print_big_comment(OUT, 4, "Protocol " . $name);

    ###########################################################################
    # Step methods
    # 
    print_comment(OUT, 4, "Step methods");

    my $ifdef_printed=0;
    foreach my $s (@$steps) {
        my $step_name = $$s{"name"};
        if($$s{"call"}) {
            my $call = $$s{"call"};
            if(!$ifdef_printed) {
                print OUT "    #if defined(JAVACARD_APPLET) || ",
                    "defined(APPLET_TESTFRAME) || defined(JAVADOC)\n\n";
                $ifdef_printed = 1;
            }

            print OUT <<EOF;
      /**
       * Card action for step $step_name in protocol $name.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class ${step_name}_call implements Void_method {
          /** Empty constructor. */
          /* package */ ${step_name}_call() {}

          /**
           * Run the card action for step $step_name in protocol $name.
           */
          public void method() { 
              $call;
              return;
          }
      }

EOF

        }
    }
    if($ifdef_printed) {
        print OUT "    #endif\n\n\n";
    }


    ###########################################################################
    # Step declarations
    # 
    print_comment(OUT, 4, "Steps");
    foreach my $s (@$steps) {
        my $step_name = $$s{"name"};
        print_field_doc(OUT, 
                        "Step instance for step $step_name " .
                        "in protocol $name.");
        print OUT "    Protocol_step ${step_name}_step;\n\n";
        push(@step_init_statements, "init_${step_name}_step();");
        push(@step_update_statements, "update_${step_name}_step();");
        push(@step_list, "${step_name}_step");
    }
    print OUT "\n\n";


    ###########################################################################
    # Steps
    # 
    my $step_counter = 0;
    foreach my $s (@$steps) {
        my $step_name = $$s{"name"};
        my $args = $$s{"args"};
        my $res = $$s{"res"};

        ######################################################################
        # Steps methods
        # 
        print OUT <<EOF;
    /**
     * Initialize {\@link #${step_name}_step}.
     * Initialize the step instance for step $step_name in protocol $name.
     */
    private void init_${step_name}_step() {
        if(${step_name}_step != null) 
            return;

EOF
        print OUT "        APDU_Serializable[] args = ";

        if(@$args) {
            print OUT "new APDU_Serializable[]{\n            ";
            print OUT join(",\n            ", @$args), "\n";
            print OUT "        };\n\n";
        }
        else {
            print OUT "null;\n\n"
        }

        print OUT "        APDU_Serializable[] res = ";

        if(@$res) {
            print OUT "new APDU_Serializable[]{\n            ";
            print OUT join(",\n            ", @$res), "\n";
            print OUT "        };\n\n";
        }
        else {
            print OUT "null;\n\n"
        }

        my $call_class = "Empty_void_method";
        if($$s{"call"}) {
            $call_class = $step_name . "_call";
        }
        print OUT <<EOF;
        ${step_name}_step = 
            new Protocol_step(
                (byte)$step_counter,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new $call_class(),      // method
                #endif
                res                   // results
            );
        return;
    }


EOF

        ######################################################################
        # Steps update methods
        # 
        print OUT <<EOF;
    /**
     * Update step instance in {\@link #${step_name}_step}.
     * Update argument and result references in the step $step_name
     * of protocol $name.
     */
    public void update_${step_name}_step() {
        ASSERT(${step_name}_step != null);

EOF
        for(my $i = 0; $i < @$args; $i++) {
            print OUT indent(8), "${step_name}_step.arguments[$i] = ",
                "$$args[$i];\n";
        }
        for(my $i = 0; $i < @$res; $i++) {
            print OUT indent(8), "${step_name}_step.results[$i] = ",
                "$$res[$i];\n";
        }

        print OUT indent(8), "return;\n", indent(4), "}\n\n\n";


        $step_counter++;
    }


    ###########################################################################
    # Protocol definition
    # 

    print OUT "\n";
    print_comment(OUT, 4, "$name protocol definition");

    print OUT <<EOF;
    /**
     * Protocol instance for protocol $name.
     */
    public Protocol ${lc_name}_protocol;

    /**
     * Initialize {\@link #${lc_name}_protocol}.
     * Initialize the protocol instance for protocol $name.
     */
    private void init_${lc_name}_protocol() {
        if(${lc_name}_protocol != null)
            return;

EOF

    if($delayed_init) {
        push(@delayed_protocol_init_statements, "init_${lc_name}_protocol();");
    }
    else {
        push(@protocol_init_statements, "init_${lc_name}_protocol();");
    }

    print_text_array(OUT, 8, @step_init_statements);
    print OUT "\n";

    print OUT "        Protocol_step[] steps = new Protocol_step[]{\n",
        "            ",
        join(",\n            ", @step_list),
        "\n        };\n";

    print OUT <<EOF;
        ${lc_name}_protocol = new Protocol(steps);
        return;
    }


EOF

    ###########################################################################
    # Protocol update
    # 
    print_field_doc(OUT, 
                    "Update {\@link #${lc_name}_protocol}.\n" .
                    "Update argument and result references in all \n" .
                    "steps of protocol $name.\n");
    print OUT indent(4), "public void update_${lc_name}_protocol() {\n";
    print_text_array(OUT, 8, @step_update_statements);
    print OUT indent(8), "${lc_name}_protocol.set_result_sizes();\n";
    print OUT indent(4), "}\n\n\n";
    push(@protocol_update_statements, "update_${lc_name}_protocol();");

}



##############################################################################
# ALL update

print_field_doc(OUT, 
                "Update all protocols in this object.\n" .
                "Update all argument and result references in all\n" .
                "steps of all protocol instances described in $file.\n");
print OUT indent(4), "public void update_all() {\n";
print_text_array(OUT, 8, @protocol_update_statements);
print OUT indent(4), "}\n\n\n";


##############################################################################
# delayed protocol initialization
# 

if(@delayed_protocol_init_statements) {
    print_field_doc(OUT, 
                    "Initialization of delayed protocols.\n" .
                    "Initialization of those protocols and their steps\n" .
                    "that are declared as delayed in $file.\n");
    print OUT indent(4), "public void delayed_init() {\n";
    print_text_array(OUT, 8, @delayed_protocol_init_statements);
    print OUT indent(4), "}\n\n\n";
}



##############################################################################
# Description class constructor
# 

print_big_comment(OUT, 4, "constructor: initialize protocols");

print_field_doc(OUT, 
                "Construct protocol descriptions.\n" .
                "Construct and initialize the protocol descriptions\n" .
                "for all protocols described in $file,\n" .
                "except for those that are declared as delayed there.\n");
print OUT "    public ${class_name}_description(";
$last_arg = pop(@constructor_arguments);
foreach my $arg (@constructor_arguments) {
    print OUT $$arg[0], " ", $$arg[1], ", ";
}
if($last_arg) {
    print OUT $$last_arg[0], " ", $$last_arg[1];
}
print OUT ") {\n";

print OUT "        // initialize variables\n";

print_text_array(OUT, 8, @variable_inits);

if(@card_variable_inits) {
    print OUT "        #if defined(JAVACARD_APPLET) || ",
        "defined(APPLET_TESTFRAME)\n";
    print_text_array(OUT, 12, @card_variable_inits);
    print OUT "        #endif\n";
}

if(@constructor_statements) {
    print OUT "\n", indent(8), "// constructor statements\n";
    print_text_array(OUT, 8, @constructor_statements);
}

print OUT "\n        // initialize protocols\n";
print_text_array(OUT, 8, @protocol_init_statements);

print OUT "        return;\n";
print OUT "    }\n}\n\n";

close OUT;



#############################################################################
#############################################################################
##
##  Generate stubs
##
#############################################################################
#############################################################################


my @host_step_init_statements;
my @delayed_host_step_init_statements;


#############################################################################
##
## stubs subroutines: hp_step_name
##
#############################################################################


sub hp_step_name_decl_and_init(*$$$) {
    my ($handle, $lc_protocol_name, $protocol, $step) = @_;

    my $name = $$protocol{"name"};
    my $step_name = $$step{"name"};
    my $message = $$step{"message"};


    my $doc_init_from_info;
    if($$protocol{"delayed_init"}) {
        $doc_init_from_info = "(which is called from {\@link #delayed_init})";
    }
    else {
        $doc_init_from_info = "(which is called from the constructor)";
    }
    print $handle <<EOF;
    /**
     * Host protocol instance for step $step_name of protocol $name.
     * Initialized via {\@link #init_hp_$step_name init_hp_$step_name} 
     * $doc_init_from_info.
     */
    private Host_protocol hp_$step_name;

    /**
     * Initialization method for {\@link #hp_$step_name}.
     *
     * \@param d description instance for $file
     * \@param out the debugging out channel, {\@code null} for disabling 
     *         debugging output
     * \@param script whether this step prints apdutool lines
     */
    private void init_hp_$step_name(${class_name}_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_$step_name = 
            new Host_protocol(d.${lc_protocol_name}_protocol,
                              d.${step_name}_step,
                              out,
                              script,
                              "$message"
                              );
    }


EOF
    my $step_init = 
      "init_hp_$step_name(protocol_description, out, with_apdu_script);";

    if($$protocol{"delayed_init"}) {
        push(@delayed_host_step_init_statements, $step_init);
    }
    else {
        push(@host_step_init_statements, $step_init);
    }
}



##########################################################################
##
## stubs subroutines: determine return type and return value
##
##########################################################################


sub ret_type_value(*$$) {
    my ($handle, $step, $protocol_name) = @_;
    
    my $step_name = $$step{"name"};
    my $res = $$step{"res"};
    my $time = $$step{"time"};

    my $ret_type = "void";
    my $ret_value = "";
    my $ret_doc = "";
    if(@$res > 1 || (@$res == 1 && $time)) {

        ######################################################################
        # Special return type declaration
        ######################################################################

        $ret_type = ucfirst($step_name) . "_result";

        # javadoc for ret type
        print_field_doc($handle, 
                        "Result record for step $step_name of\n" .
                        "protocol $protocol_name.");
        # inner class declaration header
        print $handle indent(4), "public static class $ret_type {\n";

        # field declarations
        if($time) {
            print_doc($handle, 8, 
                      "Duration of the call in nanoseconds. The measurement\n" .
                      "includes (de-)serialization but not the\n" .
                      "allocation of argument and result arrays.");
            print $handle indent(8), "public final long duration;\n\n";
        }
        foreach my $r (@$res) {
            my $r_type = $variable_type{$r};
            my $tr_type = $r_type;
            if($type_translation{$r_type}) {
                $tr_type = $type_translation{$r_type}->{interface_type};

                print_doc($handle, 8, 
                          "Return value $r converted from $r_type.");
            }
            else {
                print_doc($handle, 8, "Return value $r.");
            }
            my $r_field = $r;
            $r_field =~ s/\./_/g;
            
            print $handle indent(8), "public final ", $tr_type, " ",
                $r_field, ";\n";
        }

        # constructor
        print_doc($handle, 8, "Return record constructor.");
        print $handle indent(8), "public $ret_type(\n";
        $ret_value = "new " . $ret_type . "(";

        # Collect the constructor body during generating the arguments 
        # in @constr_assignments.
        my @constr_assignments;

        # time argument
        if($time) {
            print $handle indent(20), "long ad,\n";
            push(@constr_assignments, "duration = ad;");
            $ret_value .= "duration, ";
        }
        # make one argument out of every result value
        for(my $i = 0; $i < @$res; ) {
            my $r = $$res[$i];

            # determine interface type
            my $r_type = $variable_type{$r};
            my $r_value = rename_host_result($r);
            my $type_trans = $type_translation{$r_type};
            if($type_trans) {
                $r_type = $type_trans->{interface_type};
                $r_value .= $type_trans->{value_selector};
            }

            print $handle indent(20), $r_type, " a$i";
            my $r_field = $r;
            $r_field =~ s/\./_/g;
            push(@constr_assignments, "$r_field = a$i;");
            $ret_value .= $r_value;
            $i++;
            if($i < @$res) {
                print $handle ",\n";
                $ret_value .= ", ";
            }
            else {
                print $handle ") {\n";
                $ret_value .= ")";
            }
        }
        print_text_array($handle, 12, @constr_assignments);
        print $handle indent(8), "}\n", indent(4), "}\n\n\n";

        $ret_doc = "\@return $ret_type record containing all results";
        if($time) {
            $ret_doc .= ", including the duration of the call";
        }
        $ret_doc .= ".\n";
    }
    elsif(@$res == 1) {
        my $var_type = $variable_type{$$res[0]};
        $ret_value = rename_host_result($$res[0]);
        if($type_translation{$var_type}) {
            $t = $type_translation{$var_type};
            $ret_type = $t->{interface_type};
            $ret_value .= $t->{value_selector};
            $ret_doc = "\@return result $$res[0] converted from $var_type\n";
        }
        else {
            $ret_type = $var_type;
            $ret_doc = "\@return result $$res[0].\n";
        }
    }
    elsif($time) {
        $ret_type = "long";
        $ret_value = "duration";
        $ret_doc = "\@return Duration of the call in nanoseconds. \n" .
            "The measurement includes (de-)serialization but not the\n" .
            "allocation of argument and result arrays.\n";
    }
    # else the return type is void, see initialization obove

    return ($ret_type, $ret_value, $ret_doc);
}


##########################################################################
##
## stubs subroutines: call method
##
##########################################################################

sub rename_host_argument($) {
    my ($orig) = @_;

    $orig =~ s/\./_/g;

    return "_" . $orig . "_host_arg";
}

sub rename_host_result($) {
    my ($orig) = @_;

    $orig =~ s/\./_/g;

    return "_" . $orig . "_host_res";
}

sub card_call(*$) {
    my ($handle, $step) = @_;

    my $step_name = $$step{"name"};

    print $handle indent(8), 
        "hp_${step_name}.run_step(_cc, call_args, call_res);\n";
}


sub testframe_call(*$) {
    my ($handle, $step) = @_;

    my $step_name = $$step{"name"};
    my $args = $$step{"args"};
    my $res = $$step{"res"};

    print $handle indent(8), 
        "Convert_serializable.array_to(\n",
        indent(16), "out, \"arg[%d] = \",\n",
        indent(16), "call_args,\n",
        indent(16), "protocol_description.", $step_name, "_step.arguments);\n";

    print $handle indent(8), "protocol_description.", 
        $step_name, "_step.method.method();\n";

    print $handle indent(8), 
        "Convert_serializable.array_from(\n",
        indent(16), "out, \"res[%d] = \",\n", 
        indent(16), "call_res,\n",
        indent(16), "protocol_description.", $step_name, "_step.results);\n";
}


sub stub_call_method(*$$$$$$) {
    my ($handle, $step, $protocol_name, 
        $ret_type, $ret_value, $ret_doc, $testframe) = @_;

    my $step_name = $$step{"name"};
    my $args = $$step{"args"};
    my $res = $$step{"res"};
    my $time = $$step{"time"};
    my $message = $$step{"message"};

    my $argument_doc = "";
    my @argument_decl;
    # iterate over arguments to determine argument declarations and 
    # argument documentations.
    for($i = 0; $i < @$args; $i++) {
        my $oarg=$$args[$i];
        my $decl_type = $variable_type{$oarg};
        # rename arguments to avoid name clashes
        my $arg = rename_host_argument($oarg);

        $argument_doc .= "\@param $arg argument $oarg";

        $type_trans = $type_translation{$decl_type};
        my $interface_type = $decl_type;
        if($type_trans) {
            $interface_type = $$type_trans{interface_type};
            $argument_doc .= " to be converted to $decl_type";
        }
        $argument_doc .= "\n";
        
        push(@argument_decl, $interface_type . " " . $arg);
    }

    print_field_doc($handle,
                    "Call step $step_name of protocol $protocol_name\n" .
                    ($testframe ? "in the test environment." :
                     "on the card.") .
                    "\n\n" .
                    "\@param _cc " .
                    ($testframe ?
                     ("ignored here, can be null, card channel in " .
                      "{\@link ${class_name}_stubs}\n") :
                     ("communication channel to the applet, must " .
                      "not be null\n")) .
                    $argument_doc .
                    $ret_doc .
                    ($testframe ? "" :
                     "\@throws CardException in case of " .
                     "communication errors"));
                    
    $call_start = "    public $ret_type ${step_name}_call(";
    print $handle $call_start;
    $arg_indent = indent(length($call_start));
    print $handle "CardChannel _cc";
    foreach my $decl (@argument_decl) {
        print $handle ",\n", $arg_indent, $decl;
    }

    # remaining header of stub call method
    print $handle ")\n";
    print $handle indent(8), "throws CardException\n" unless $testframe;
    print $handle indent(4), "{\n";

    if($testframe) {
        print $handle <<EOF;
        if(out != null) 
            out.println("start step $message");
EOF
    }

    # argument array
    print $handle indent(8), "APDU_Serializable[] call_args = ";
    if(@$args) {
        print $handle "new APDU_Serializable[]{\n";

        for($i = 0; $i < @$args;) {
            my $arg = $$args[$i];
            my $interface_type = $variable_type{$arg};
            my $apdu_type = $interface_type;

            # Rename arguments to avoid name clashes
            my $renamed_arg = rename_host_argument($arg);

            $type_trans = $type_translation{$apdu_type};
            if($type_trans) {
                $interface_type = $$type_trans{interface_type};
                $apdu_type = $$type_trans{apdu_type};
            }
            if($interface_type eq $apdu_type) {
                print $handle indent(12), $renamed_arg;
            }
            else {
                print $handle indent(12), "new ", $apdu_type;
                my $arg_inits = $host_stub_initializer{$arg};
                if($arg_inits) {
                    my $qualified_arg = "protocol_description." . $arg;
                    print $handle "(",
                         map($qualified_arg . "." . $_ . ", ", @$arg_inits),
                         $renamed_arg, ")";
                }
                else {
                    print $handle "($renamed_arg)";
                }
            }
            $i++;
            if($i < @$args) {
                print $handle ",";
            }
            print $handle "\n";
        }
        print $handle indent(8), "};\n\n";

    }
    else {
        print $handle "null;\n\n"
    }

    # result declarations
    foreach my $r (@$res) {
        my $apdu_type = $variable_type{$r};

        my $type_trans = $type_translation{$apdu_type};
        if($type_trans) {
            $apdu_type = $$type_trans{apdu_type};
        }
        my $init_expr = "()";
        my $res_inits = $host_stub_initializer{$r};
        if($res_inits) {
            my $qualified_res = "protocol_description." . $r;
            $init_expr = "(" . 
                join(", ", map($qualified_res . "." . $_, @$res_inits))
                . ")";
        }
        print $handle indent(8), $apdu_type, " ", rename_host_result($r), 
            " = new ", $apdu_type, $init_expr, ";\n";
    }

    #result array
    print $handle indent(8), "APDU_Serializable[] call_res = ";
    if(@$res) {
        print $handle "new APDU_Serializable[]{\n";

        for($i = 0; $i < @$res;) {
            $r=$$res[$i];
            print $handle indent(12), rename_host_result($r);
            $i++;
            if($i < @$res) {
                print $handle ",";
            }
            print $handle "\n";
        }
        print $handle indent(8), "};\n\n";

    }
    else {
        print $handle "null;\n\n"
    }

    if($time) {
        print $handle indent(8), "long start = System.nanoTime();\n";
    }
    if($testframe) {
        testframe_call(OUT, $step);
    }
    else {
        card_call(OUT, $step);
    }
    if($time) {
        print $handle indent(8), "long duration = System.nanoTime() - start;\n";
    }


    if($testframe) {
        print $handle <<EOF;
        if(out != null)
            out.println("finished step $message");
EOF
    }


    print $handle indent(8), "return";
    print $handle " ", $ret_value if $ret_value ne "";
    print $handle ";\n";

    print $handle "    }\n\n\n";

}


##########################################################################
##
##  start host stub generation
##
##########################################################################


if($dir) {
    $stub_file = $dir . "/" . $class_name . "_stubs.java";
}
else {
    $stub_file = $class_name . "_stubs.java";
}    
print("Generating $stub_file\n");
$mask=umask;
umask($mask | 0222);
unlink $stub_file;
open(OUT, ">", $stub_file) || die "Cannot open $stub_file";
umask($mask);


#############################################################################
## stubs header
##

print_header(OUT);

print OUT <<EOF;
package $package;

import java.io.PrintWriter;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import ds.ov2.util.APDU_Serializable;
import ds.ov2.util.Host_protocol;
EOF

print_text_array(OUT, 0, @host_imports);

print OUT "\n\n";

print_class_doc(OUT, 
                "Stub code for running methods on the card.\n" .
                "Defines one stub method for each protocol step in $file.\n" .
                "The stub methods are the top entry point into the\n" .
                "OV-chip protocol layer for host driver code.\n" .
                "Each stub method performs the following actions:\n" .
                "<OL>\n" .
                "<LI>argument conversion (for instance from\n" .
                "    {\@link java.math.BigInteger BigInteger} to\n" .
                "    {\@link ds.ov2.bignat.Bignat Bignat})</LI>\n" .
                "<LI>transfers the arguments to the card\n" .
                "    (possibly using several APDU's)</LI>\n" .
                "<LI>invokes the right method on the card</LI>\n" .
                "<LI>transfers the results back (again with possibly\n" .
                "    several APDU's)</LI>\n" .
                "<LI>result conversion</LI>\n" .
                "<LI>and finally packages several results into one\n" .
                "    tuple object</LI>\n" .
                "</OL>",
                "host",
                ());

print OUT <<EOF;
public class ${class_name}_stubs {

    /**
     * A protocol description instance from $file. Used to access
     * the host initializers, which are additional parameters for
     * the APDU type constructors of arguments or results.
     * Initialized in the constructor.
     */
    private ${class_name}_description protocol_description;


    /**
     * The output channel for debugging information of the 
     * OV-chip protocol layer.
     * Initialized in the constructor.
     */
    private PrintWriter out;


    /**
     * Controls apdutool line printing. Initialized in the constructor,
     * if true, the OV-chip protocol layer prints apdutool lines as 
     * part of its debugging output.
     */
    private boolean with_apdu_script;

EOF


foreach my $p (@protocols) {
    my $name = $$p{"name"};
    my $lc_name = lc($name);
    my $steps=$$p{"steps"};
    print_big_comment(OUT, 4, "Protocol " . $name);

    ##########################################################################
    ##
    ##  hp_step declaration / initialization
    ##
    ##########################################################################

    foreach my $s (@$steps) {
        my $step_name = $$s{"name"};

        print_comment(OUT, 4, "Step $step_name");

        hp_step_name_decl_and_init(OUT, $lc_name, $p, $s);

        # get return type and value
        my ($ret_type, $ret_value, $ret_doc) = ret_type_value(OUT, $s, $name);

        stub_call_method(OUT, $s, $name, $ret_type, $ret_value, $ret_doc, 0);
    }
}


###############################################################################
##
##  delayed initialization

print_comment(OUT, 4, "Delayed stub initialization");

if(@delayed_host_step_init_statements) {
    print_field_doc(OUT,
                    "Delayed initialization.\n" .
                    "Initialize protocols annotated with\n" .
                    "<EM>delayed protocol init</EM> in $file.");
    print OUT indent(4), "public void delayed_init() {\n";
    print_text_array(OUT, 8, @delayed_host_step_init_statements);
    print OUT indent(4), "}\n\n\n";
}



###############################################################################
##
##  stubs constructor
##
###############################################################################


print_comment(OUT, 4, "Constructor");

if(@delayed_host_step_init_statements) {
    print OUT <<EOF;
    /**
     * Stub constructor. Initializes all non-delayed host protocol
     * instances from $file. Delayed protocols must be initialized
     * separately with {\@link #delayed_init} at the appropriate moment.
EOF
}
else {
    print OUT <<EOF;
    /**
     * Stub constructor. Initializes all host protocol
     * instances from $file. 
EOF
}

print OUT <<EOF;
     *
     * \@param d protocol description instance for $file
     * \@param o channel for printing debugging information, pass null 
     *           for disabling debugging information
     * \@param script if true, print apdutool lines for all APDUs as part 
     *          of the debugging information.
     */
    public ${class_name}_stubs(${class_name}_description d,
                               PrintWriter o, 
                               boolean script) {
        protocol_description = d;
        out = o;
        with_apdu_script = script;
        // initialize the Host_protocols
EOF

print_text_array(OUT, 8, @host_step_init_statements);

print OUT <<EOF;
    }
}

EOF


close OUT;



#############################################################################
#############################################################################
##
##  Generate testframe stubs
##
#############################################################################
#############################################################################


if($dir) {
    $stub_file = $dir . "/" . $class_name . "_test_stubs.java";
}
else {
    $stub_file = $class_name . "_test_stubs.java";
}    
print("Generating $stub_file\n");
$mask=umask;
umask($mask | 0222);
unlink $stub_file;
open(OUT, ">", $stub_file) || die "Cannot open $stub_file";
umask($mask);


#############################################################################
## testframe stubs header
##

print_header(OUT);

print OUT <<EOF;
package $package;

import java.io.PrintWriter;
import javax.smartcardio.CardChannel;
import ds.ov2.util.APDU_Serializable;
import ds.ov2.util.Convert_serializable;
EOF

print_text_array(OUT, 0, @host_imports);


print OUT "\n\n";

print_class_doc(OUT, 
                "Stub code for running methods in the\n" .
                "testframe on the host.\n" .
                "Defines one stub method for each protocol step in $file.\n" .
                "This class is the test-frame alternative to\n" .
                "{\@link ${class_name}_stubs}. It provides the same\n" .
                "public interface, but instead of communicating with\n" .
                "a Java Card and invoking methods there, it directly\n" .
                "invokes the methods on the very same Java Virtual\n" . 
                "machine. This is of course only possible, if (almost)\n" .
                "all of the applet code is available on the host.\n" .
                "All {\@link javax.smartcardio.CardChannel\n" .
                "CardChannel} arguments in this class are unused." .
                "<P>\n\n" .
                "Each stub method here performs the following actions:\n" .
                "<OL>\n" .
                "<LI>argument conversion (for instance from\n" .
                "    {\@link java.math.BigInteger BigInteger} to\n" .
                "    {\@link ds.ov2.bignat.Bignat Bignat})</LI>\n" .
                "<LI>running the specified action for this step</LI>\n" .
                "<LI>result conversion</LI>\n" .
                "<LI>and finally packages several results into one\n" .
                "    tuple object</LI>\n" .
                "</OL>",
                "host",
                ());


print OUT <<EOF;
public class ${class_name}_test_stubs {

    /**
     * A protocol description instance from $file. Used to access
     * the host initializers, which are additional parameters for
     * the APDU type constructors of arguments or results.
     * Initialized in the constructor.
     */
    private ${class_name}_description protocol_description;

    /**
     * The output channel for debugging information of 
     * the OV-chip protocol layer.
     * Initialized in the constructor.
     */
    private PrintWriter out = null;

EOF

my $has_delayed_init_method = 0;

foreach my $p (@protocols) {
    my $name = $$p{"name"};
    my $steps=$$p{"steps"};
    print_big_comment(OUT, 4, "Protocol " . $name);

    ##########################################################################
    ##
    ##  testframe stubs ??
    ##
    ##########################################################################

    foreach my $s (@$steps) {
        my $step_name = $$s{"name"};
        my $args = $$s{"args"};
        my $res = $$s{"res"};
        my $message = $$s{"message"};

        print_comment(OUT, 4, "Step $step_name");

        # get return type and value
        my ($ret_type, $ret_value, $ret_doc) = ret_type_value(OUT, $s, $name);

        stub_call_method(OUT, $s, $name, $ret_type, $ret_value, $ret_doc, 1);

    }

    if($$p{"delayed_init"}) {
        $has_delayed_init_method = 1;
    }
}


###############################################################################
##
##  delayed initialization

print_comment(OUT, 4, "Delayed stub initialization");

if($has_delayed_init_method) {
    print_field_doc(OUT,
                    "Delayed initialization.\n" .
                    "Empty method, only here for compatibility with\n" .
                    "{\@link ${class_name}_stubs}. In this test-frame\n" .
                    "alternative there are no {\@link \n" .
                    "ds.ov2.util.Host_protocol Host_protocol} data\n" .
                    "structures to initialize, consequently nothing has\n" .
                    "to be done in delayed initialization");
    print OUT indent(4), "public void delayed_init() {\n";
    print OUT indent(4), "}\n\n\n";
}


###############################################################################
##
##  testframe stubs constructor
##
###############################################################################


print_comment(OUT, 4, "Constructor");

print OUT <<EOF;
    /**
     * Stub constructor. In this test-frame alternative there are
     * no {\@link ds.ov2.util.Host_protocol Host_protocol} data 
     * structures to initialize. Therefore this constructor only
     * saves the first two arguments into object local fields.
     * The argument {\@code script} is only there for compatibility
     * with {\@link ${class_name}_stubs}, it is ignored here.
     *
     * \@param d protocol description instance for $file
     * \@param o channel for printing debugging information, pass null 
     *           for disabling debugging information
     * \@param script ignored here, controls printing of  apdutool lines
     *           in {\@link ${class_name}_stubs}
     */
     
    public ${class_name}_test_stubs(${class_name}_description d,
                                    PrintWriter o, 
                                    boolean script) {
        out = o;
        protocol_description = d;
        return;
    }
}

EOF

close OUT;


### Local Variables:
### eval: (add-hook 'write-contents-hooks 'untabify-buffer)
### End:
