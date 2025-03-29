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
    print "usage: $0 -p project_id -v version_id -w work_dir\n";
    exit 1;
}

my %cmd_opts;
getopts('p:v:w:', \%cmd_opts) or _usage();

my $PID = $cmd_opts{p};
my $VID = $cmd_opts{v};
print("$PID\_$VID\n");
my $WORK_DIR = Utils::get_abs_path($cmd_opts{w});
my $project = Project::create_project($PID);

# print(Utils::check_vid($VID)->{type});
# print("\n");
$project->{prog_root} = $WORK_DIR;
# print($project->_determine_layout("362fa778259f188a4bef82716478d9e288d3f303")->{src});
# print("\n");
# print($project->{_vcs}->{_cache}->{4}->{i});
# print("\n");
printf("%s\n%s\n",$project->src_dir($VID),$project->test_dir($VID));
my $BID = Utils::check_vid($VID)->{bid};
my $type = Utils::check_vid($VID)->{type};

# write config
Utils::write_config_file("$WORK_DIR/$CONFIG", {$CONFIG_PID => $PID, $CONFIG_VID => "${BID}${type}"});
# write properties
$project->_write_props($VID, 0);
