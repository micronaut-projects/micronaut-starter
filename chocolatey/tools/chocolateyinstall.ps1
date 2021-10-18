$version = '3.1.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '99E374A19F21B59B1D2B13AE2B16559D57DE5AB62FED31742D1DFE81F6890B9D'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
