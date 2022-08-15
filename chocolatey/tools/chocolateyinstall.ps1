$version = '3.6.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '5C03C0CE6049A71BF3722DFAF5E5F126AFF4E5CBEDE7D2956214AF2910343F07'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
