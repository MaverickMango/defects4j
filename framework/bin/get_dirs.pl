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
    print "usage: $0 -p project_id -v version_id -c commit_id -w work_dir\n";
    exit 1;
}

my %cmd_opts;
getopts('p:v:c:w:', \%cmd_opts) or _usage();

my $PID = $cmd_opts{p};
my $VID = $cmd_opts{v};
my $COMMIT = $cmd_opts{c};
my $WORK_DIR = Utils::get_abs_path($cmd_opts{w});
my $project = Project::create_project($PID);

$project->{prog_root} = $WORK_DIR;
my $BID = Utils::check_vid($VID)->{bid};
my $SRCDIR = $project->_determine_layout($COMMIT)->{src};
my $TESTDIR = $project->_determine_layout($COMMIT)->{test};
my $config = {"d4j.bug.id" => $BID,"d4j.dir.src.classes" => $SRCDIR, "d4j.dir.src.tests" => $TESTDIR};
Utils::write_config_file("$WORK_DIR/dirs", $config);
print('Finished.')