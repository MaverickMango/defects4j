#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

use FindBin qw($Bin);
use File::Basename;
use Cwd qw(abs_path);
use Getopt::Std;

use lib abs_path("$Bin/../core");
use Constants;
use Utils;
use Project;

#
# Issue usage message and quit
#
sub _usage {
    print "usage: $0 -p project_id -v version_id -w work_dir
     -t test_dir 
     [-l log_file -i include_file -s single_test_name='<classname>::<methodname>' -o failing_output]\n";
    exit 1;
}

my %cmd_opts;
getopts('p:v:w:t:s:l:o:i:', \%cmd_opts) or _usage();

my $PID = $cmd_opts{p};
my $VID = $cmd_opts{v};
my $BID = Utils::check_vid($VID)->{bid};
my $WORK_DIR = Utils::get_abs_path($cmd_opts{w});
my $TEST_DIR = Utils::get_abs_path($cmd_opts{t});

my $LOG_FILE;
if ($cmd_opts{l}) {
    $LOG_FILE = $cmd_opts{l};
}
# printf("%s", $LOG_FILE);
my $SINGLE_TEST_NAME;
if ($cmd_opts{s}) {
    $SINGLE_TEST_NAME = $cmd_opts{s};
}
my $FAILING_OUTPUT;
if ($cmd_opts{o}) {
    $FAILING_OUTPUT = $cmd_opts{o};
}
my $INCLUDE;
if ($cmd_opts{i}) {
    $INCLUDE = $cmd_opts{i};
}

my $project = Project::create_project($PID);
$project->{prog_root} = $WORK_DIR;

# my $SRCDIR = $project->_determine_layout($COMMIT)->{src};
# my $TESTDIR = $project->_determine_layout($COMMIT)->{test};

# $project->compile_ext_tests(test_dir [, log_file])
# $project->compile_external_tests($TEST_DIR, $LOG_FILE);
my $tests_result = $project->run_external_tests($TEST_DIR, $LOG_FILE, $INCLUDE, $FAILING_OUTPUT, $SINGLE_TEST_NAME);
printf("%s\n", $tests_result) 