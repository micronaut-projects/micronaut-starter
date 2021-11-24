$version = '3.2.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '92CBA6C11C42E269469ACD85EDBD949484A8313CC6C1CA9D85E3CAD12030D444'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
