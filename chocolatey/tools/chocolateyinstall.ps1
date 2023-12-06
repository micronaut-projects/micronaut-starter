$version = '4.2.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '72180FCE41107D7F81B6B4A749EEFE7304D48B6F969C5B55FE4ECAF53CF31C0C'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
