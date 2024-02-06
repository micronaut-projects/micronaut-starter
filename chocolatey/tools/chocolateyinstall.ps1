$version = '4.3.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'AD6C9AC1DAE6C83EE23A9B076C8424CD6D5FB5FA1AA6AACF895C77643ACDD2AB'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
