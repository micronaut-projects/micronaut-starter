$version = '3.4.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '94F2349A5430B9F2E5B2584836DF5AF8FF2F2066ED8FBF3C93FAC54772D9D271'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
