$version = '3.6.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '17C0FDEFC15AE8F92131CDA58F57FD98AC69B80D942D993D8DFD1D313BECB07F'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
