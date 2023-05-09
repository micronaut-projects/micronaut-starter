$version = '4.0.0-M2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '4D91AF816320BD3C409A3B8AC458F30AB1A14619FC1802572C94DD04E8EAD475'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
