$version = '3.2.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '20147B7795E61480D0FAF8DF0A5BAA7021D8E0A5D49579EA184F44AE99CFA73B'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
