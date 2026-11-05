$version = '5.2.5'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '05272BB4FAFA542828A9B4D0A7383BDE5254BC783958C628193A3328948F32AE'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
